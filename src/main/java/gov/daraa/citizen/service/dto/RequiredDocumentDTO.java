package gov.daraa.citizen.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link gov.daraa.citizen.domain.RequiredDocument} entity.
 */
@Schema(description = "الأوراق المطلوبة لكل خدمة")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequiredDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Boolean mandatory;

    @Min(value = 0)
    private Integer orderIndex;

    private CitizenServiceDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public CitizenServiceDTO getService() {
        return service;
    }

    public void setService(CitizenServiceDTO service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequiredDocumentDTO)) {
            return false;
        }

        RequiredDocumentDTO requiredDocumentDTO = (RequiredDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requiredDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequiredDocumentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", service=" + getService() +
            "}";
    }
}
