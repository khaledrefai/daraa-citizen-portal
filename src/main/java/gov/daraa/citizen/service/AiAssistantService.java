package gov.daraa.citizen.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.daraa.citizen.config.AiAssistantProperties;
import gov.daraa.citizen.service.dto.AssistantChatRequest;
import gov.daraa.citizen.service.dto.AssistantChatResponse;
import gov.daraa.citizen.service.dto.AssistantSource;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.dto.RequiredDocumentDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Service
public class AiAssistantService {

    private static final Logger LOG = LoggerFactory.getLogger(AiAssistantService.class);

    private final AiAssistantProperties properties;
    private final RestClient openAiClient;
    private final RestClient qdrantClient;
    private final AtomicBoolean collectionPrepared = new AtomicBoolean(false);

    public AiAssistantService(AiAssistantProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.openAiClient = restClientBuilder
            .clone()
            .baseUrl(properties.getOpenAi().getBaseUrl())
            .defaultHeaders(headers -> {
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (StringUtils.hasText(properties.getOpenAi().getApiKey())) {
                    headers.setBearerAuth(properties.getOpenAi().getApiKey());
                }
            })
            .build();
        this.qdrantClient = restClientBuilder
            .clone()
            .baseUrl(properties.getQdrant().getUrl())
            .defaultHeaders(headers -> {
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (StringUtils.hasText(properties.getQdrant().getApiKey())) {
                    headers.set("api-key", properties.getQdrant().getApiKey());
                }
            })
            .build();
    }

    public boolean isEnabled() {
        return (
            properties.isEnabled() &&
            StringUtils.hasText(properties.getOpenAi().getApiKey()) &&
            StringUtils.hasText(properties.getOpenAi().getBaseUrl()) &&
            StringUtils.hasText(properties.getQdrant().getUrl()) &&
            StringUtils.hasText(properties.getQdrant().getCollection())
        );
    }

    public AssistantChatResponse chat(AssistantChatRequest request) {
        if (!isEnabled()) {
            return new AssistantChatResponse("المساعد الذكي غير مفعل حالياً.", List.of());
        }

        try {
            List<Double> queryVector = embedText(request.getQuestion());
            List<AssistantSource> sources = searchSimilarServices(queryVector);
            String context = buildContextPrompt(sources);
            String answer = requestAnswerFromModel(request.getQuestion(), context);
            return new AssistantChatResponse(answer, sources);
        } catch (Exception exception) {
            LOG.warn("AI assistant chat failed: {}", exception.getMessage());
            LOG.debug("AI assistant chat failed", exception);
            return new AssistantChatResponse("تعذر معالجة الطلب حالياً، يرجى المحاولة لاحقاً.", List.of());
        }
    }

    public void indexCitizenService(CitizenServiceDTO citizenServiceDTO) {
        if (!isEnabled() || citizenServiceDTO.getId() == null) {
            return;
        }

        try {
            prepareCollection();
            String document = buildServiceDocument(citizenServiceDTO);
            List<Double> embedding = embedText(document);
            Map<String, Object> payload = buildPayload(citizenServiceDTO);
            upsertPoint(citizenServiceDTO.getId(), embedding, payload);
        } catch (Exception exception) {
            LOG.warn("Failed to index citizen service {}: {}", citizenServiceDTO.getId(), exception.getMessage());
            LOG.debug("Failed to index citizen service", exception);
        }
    }

    public void deleteCitizenServiceVector(Long id) {
        if (!isEnabled() || id == null) {
            return;
        }

        try {
            qdrantClient
                .post()
                .uri("/collections/{collection}/points/delete", properties.getQdrant().getCollection())
                .body(new QdrantDeleteRequest(List.of(id)))
                .retrieve()
                .toBodilessEntity();
        } catch (Exception exception) {
            LOG.warn("Failed to delete vector for citizen service {}: {}", id, exception.getMessage());
            LOG.debug("Failed to delete vector for citizen service", exception);
        }
    }

    private List<Double> embedText(String text) {
        EmbeddingResponse response = openAiClient
            .post()
            .uri("/embeddings")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .body(new EmbeddingRequest(properties.getOpenAi().getEmbeddingsModel(), List.of(text)))
            .retrieve()
            .body(EmbeddingResponse.class);

        if (response == null || CollectionUtils.isEmpty(response.data()) || CollectionUtils.isEmpty(response.data().get(0).embedding())) {
            throw new IllegalStateException("OpenAI embeddings response is empty");
        }

        return response.data().get(0).embedding();
    }

    private String requestAnswerFromModel(String question, String context) {
        List<OpenAiMessage> messages = new ArrayList<>();
        messages.add(
            new OpenAiMessage(
                "system",
                "أنت مساعد افتراضي لبوابة خدمات المواطن في محافظة درعا. استخدم المعلومات المتاحة ضمن سياق الخدمات فقط وأجب بالعربية بوضوح وباختصار." +
                " إذا لم تجد إجابة في السياق فأخبر المستخدم بذلك ولا تخترع معلومات جديدة."
            )
        );

        StringBuilder userPrompt = new StringBuilder();
        if (StringUtils.hasText(context)) {
            userPrompt.append("المعرفة المتوفرة عن الخدمات:\n").append(context).append("\n\n");
        }
        userPrompt.append("سؤال المستخدم: ").append(question);

        messages.add(new OpenAiMessage("user", userPrompt.toString()));

        OpenAiChatResponse response = openAiClient
            .post()
            .uri("/chat/completions")
            .body(new ChatCompletionRequest(properties.getOpenAi().getChatModel(), messages, properties.getOpenAi().getTemperature()))
            .retrieve()
            .body(OpenAiChatResponse.class);

        if (response == null || CollectionUtils.isEmpty(response.choices()) || response.choices().get(0).message() == null) {
            throw new IllegalStateException("OpenAI chat response is empty");
        }

        return response.choices().get(0).message().content();
    }

    private String buildContextPrompt(List<AssistantSource> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return "";
        }

        return sources
            .stream()
            .map(source -> {
                StringBuilder builder = new StringBuilder();
                builder.append("- ").append(source.getName());
                if (StringUtils.hasText(source.getDescription())) {
                    builder.append(": ").append(source.getDescription());
                }
                return builder.toString();
            })
            .collect(Collectors.joining("\n"));
    }

    private List<AssistantSource> searchSimilarServices(List<Double> vector) {
        QdrantSearchRequest searchRequest = new QdrantSearchRequest(vector, properties.getQdrant().getSearchLimit(), true);
        QdrantSearchResponse response = qdrantClient
            .post()
            .uri("/collections/{collection}/points/search", properties.getQdrant().getCollection())
            .body(searchRequest)
            .retrieve()
            .body(QdrantSearchResponse.class);

        if (response == null || CollectionUtils.isEmpty(response.result())) {
            return List.of();
        }

        return response
            .result()
            .stream()
            .map(point -> {
                Map<String, Object> payload = point.payload();
                String description = payload != null ? (String) payload.getOrDefault("description", "") : "";
                String name = payload != null ? (String) payload.getOrDefault("name", "خدمة") : "خدمة";
                Long serviceId;
                if (payload != null && payload.get("serviceId") != null) {
                    Object rawId = payload.get("serviceId");
                    serviceId = rawId instanceof Number ? ((Number) rawId).longValue() : Long.parseLong(rawId.toString());
                } else {
                    serviceId = point.id();
                }
                return new AssistantSource(serviceId, name, description, point.score());
            })
            .toList();
    }

    private void upsertPoint(Long id, List<Double> vector, Map<String, Object> payload) {
        QdrantPoint point = new QdrantPoint(id, vector, payload);
        qdrantClient
            .put()
            .uri("/collections/{collection}/points?wait=true", properties.getQdrant().getCollection())
            .body(new QdrantUpsertRequest(List.of(point)))
            .retrieve()
            .toBodilessEntity();
    }

    private Map<String, Object> buildPayload(CitizenServiceDTO citizenServiceDTO) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("serviceId", citizenServiceDTO.getId());
        payload.put("name", citizenServiceDTO.getName());
        payload.put("description", citizenServiceDTO.getDescription());
        payload.put("category", citizenServiceDTO.getCategory() != null ? citizenServiceDTO.getCategory().getName() : "");
        payload.put(
            "directorate",
            citizenServiceDTO.getCategory() != null && citizenServiceDTO.getCategory().getDirectorate() != null
                ? citizenServiceDTO.getCategory().getDirectorate().getName()
                : ""
        );
        payload.put("feesDescription", citizenServiceDTO.getFeesDescription());
        payload.put("isElectronic", citizenServiceDTO.getIsElectronic());
        payload.put("requiresPhysicalPresence", citizenServiceDTO.getRequiresPhysicalPresence());
        payload.put("hasSmartCard", citizenServiceDTO.getHasSmartCard());

        List<String> documentNames = citizenServiceDTO
            .getRequiredDocuments()
            .stream()
            .map(RequiredDocumentDTO::getName)
            .filter(StringUtils::hasText)
            .toList();
        payload.put("requiredDocuments", documentNames);
        return payload;
    }

    private String buildServiceDocument(CitizenServiceDTO citizenServiceDTO) {
        StringBuilder builder = new StringBuilder();
        builder.append("اسم الخدمة: ").append(citizenServiceDTO.getName()).append(". ");
        if (StringUtils.hasText(citizenServiceDTO.getDescription())) {
            builder.append("الوصف: ").append(citizenServiceDTO.getDescription()).append(". ");
        }
        if (citizenServiceDTO.getCategory() != null) {
            builder.append("التصنيف: ").append(citizenServiceDTO.getCategory().getName()).append(". ");
        }
        if (citizenServiceDTO.getCategory() != null && citizenServiceDTO.getCategory().getDirectorate() != null) {
            builder.append("الجهة: ").append(citizenServiceDTO.getCategory().getDirectorate().getName()).append(". ");
        }
        if (StringUtils.hasText(citizenServiceDTO.getFeesDescription())) {
            builder.append("الرسوم: ").append(citizenServiceDTO.getFeesDescription()).append(". ");
        }

        if (!CollectionUtils.isEmpty(citizenServiceDTO.getRequiredDocuments())) {
            builder.append("المستندات المطلوبة: ");
            builder.append(
                citizenServiceDTO
                    .getRequiredDocuments()
                    .stream()
                    .map(RequiredDocumentDTO::getName)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining(", "))
            );
            builder.append(". ");
        }

        builder
            .append("إلكترونية: ")
            .append(Boolean.TRUE.equals(citizenServiceDTO.getIsElectronic()) ? "نعم" : "لا")
            .append("، تتطلب حضور: ")
            .append(Boolean.TRUE.equals(citizenServiceDTO.getRequiresPhysicalPresence()) ? "نعم" : "لا")
            .append("، بطاقة ذكية: ")
            .append(Boolean.TRUE.equals(citizenServiceDTO.getHasSmartCard()) ? "نعم" : "لا")
            .append('.');

        return builder.toString();
    }

    private void prepareCollection() {
        if (collectionPrepared.get()) {
            return;
        }

        try {
            Map<String, Object> body = Map.of("vectors", Map.of("size", properties.getQdrant().getVectorSize(), "distance", "Cosine"));
            qdrantClient
                .put()
                .uri("/collections/{collection}", properties.getQdrant().getCollection())
                .body(body)
                .retrieve()
                .toBodilessEntity();
            collectionPrepared.set(true);
        } catch (Exception exception) {
            LOG.debug("Skipping collection preparation: {}", exception.getMessage());
        }
    }

    private record EmbeddingRequest(String model, List<String> input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingResponse(List<EmbeddingData> data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingData(List<Double> embedding) {}

    private record ChatCompletionRequest(String model, List<OpenAiMessage> messages, double temperature) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenAiChatResponse(List<ChatChoice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatChoice(OpenAiMessage message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenAiMessage(String role, String content) {}

    private record QdrantPoint(Long id, List<Double> vector, Map<String, Object> payload) {}

    private record QdrantUpsertRequest(List<QdrantPoint> points) {}

    private record QdrantSearchRequest(List<Double> vector, int limit, @JsonProperty("with_payload") boolean withPayload) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record QdrantSearchResponse(List<QdrantScoredPoint> result) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record QdrantScoredPoint(Long id, Double score, Map<String, Object> payload) {}

    private record QdrantDeleteRequest(List<Long> points) {}
}
