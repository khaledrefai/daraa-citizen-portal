package gov.daraa.citizen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.assistant", ignoreUnknownFields = false)
public class AiAssistantProperties {

    private boolean enabled = true;

    private final OpenAi openAi = new OpenAi();
    private final Qdrant qdrant = new Qdrant();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public OpenAi getOpenAi() {
        return openAi;
    }

    public Qdrant getQdrant() {
        return qdrant;
    }

    public static class OpenAi {

        private String apiKey;
        private String baseUrl;
        private String chatModel;
        private String embeddingsModel;
        private double temperature = 0.2;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getChatModel() {
            return chatModel;
        }

        public void setChatModel(String chatModel) {
            this.chatModel = chatModel;
        }

        public String getEmbeddingsModel() {
            return embeddingsModel;
        }

        public void setEmbeddingsModel(String embeddingsModel) {
            this.embeddingsModel = embeddingsModel;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
    }

    public static class Qdrant {

        private String url;
        private String apiKey;
        private String collection;
        private int batchSize = 32;
        private int searchLimit = 12;
        private int vectorSize = 1536;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getSearchLimit() {
            return searchLimit;
        }

        public void setSearchLimit(int searchLimit) {
            this.searchLimit = searchLimit;
        }

        public int getVectorSize() {
            return vectorSize;
        }

        public void setVectorSize(int vectorSize) {
            this.vectorSize = vectorSize;
        }
    }
}
