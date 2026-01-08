package gov.daraa.citizen.web.rest;

import gov.daraa.citizen.service.AiAssistantService;
import gov.daraa.citizen.service.dto.AssistantChatRequest;
import gov.daraa.citizen.service.dto.AssistantChatResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AiAssistantResource {

    private static final Logger LOG = LoggerFactory.getLogger(AiAssistantResource.class);

    private final AiAssistantService aiAssistantService;

    public AiAssistantResource(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AssistantChatResponse> chat(@Valid @RequestBody AssistantChatRequest request) {
        LOG.debug("REST request to chat with assistant");
        if (!aiAssistantService.isEnabled()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new AssistantChatResponse("المساعد الذكي غير مفعّل حالياً.", java.util.List.of())
            );
        }
        AssistantChatResponse response = aiAssistantService.chat(request);
        return ResponseEntity.ok(response);
    }
}
