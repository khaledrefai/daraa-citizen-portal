package gov.daraa.citizen.service.dto;

public class AssistantSource {

    private Long serviceId;
    private String name;
    private String description;
    private Double score;

    public AssistantSource() {}

    public AssistantSource(Long serviceId, String name, String description, Double score) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.score = score;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
