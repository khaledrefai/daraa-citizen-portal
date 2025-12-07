package gov.daraa.citizen.service.dto;

import gov.daraa.citizen.domain.enumeration.EstimatedTimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link gov.daraa.citizen.domain.CitizenService} entity.
 */
@Schema(
    description = "خدمة المواطن\nتحتوي على التصنيف، الوصف، الزمن المقدر، الأوراق المطلوبة،\nوهل هي إلكترونية، وهل تتطلب حضور شخصي، وهل لها نموذج (كرتوني/ورقي) مرفق."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitizenServiceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String code;

    @NotNull
    @Size(max = 200)
    private String name;

    @Lob
    private String description;

    @NotNull
    @Min(value = 1)
    private Integer estimatedDuration;

    @NotNull
    private EstimatedTimeUnit estimatedDurationUnit;

    @NotNull
    private Boolean requiresPhysicalPresence;

    @NotNull
    private Boolean isElectronic;

    @NotNull
    private Boolean hasSmartCard;

    @Size(max = 1000)
    private String feesDescription;

    @NotNull
    private Boolean active;

    private ServiceCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public EstimatedTimeUnit getEstimatedDurationUnit() {
        return estimatedDurationUnit;
    }

    public void setEstimatedDurationUnit(EstimatedTimeUnit estimatedDurationUnit) {
        this.estimatedDurationUnit = estimatedDurationUnit;
    }

    public Boolean getRequiresPhysicalPresence() {
        return requiresPhysicalPresence;
    }

    public void setRequiresPhysicalPresence(Boolean requiresPhysicalPresence) {
        this.requiresPhysicalPresence = requiresPhysicalPresence;
    }

    public Boolean getIsElectronic() {
        return isElectronic;
    }

    public void setIsElectronic(Boolean isElectronic) {
        this.isElectronic = isElectronic;
    }

    public Boolean getHasSmartCard() {
        return hasSmartCard;
    }

    public void setHasSmartCard(Boolean hasSmartCard) {
        this.hasSmartCard = hasSmartCard;
    }

    public String getFeesDescription() {
        return feesDescription;
    }

    public void setFeesDescription(String feesDescription) {
        this.feesDescription = feesDescription;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitizenServiceDTO)) {
            return false;
        }

        CitizenServiceDTO citizenServiceDTO = (CitizenServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citizenServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitizenServiceDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", estimatedDuration=" + getEstimatedDuration() +
            ", estimatedDurationUnit='" + getEstimatedDurationUnit() + "'" +
            ", requiresPhysicalPresence='" + getRequiresPhysicalPresence() + "'" +
            ", isElectronic='" + getIsElectronic() + "'" +
            ", hasSmartCard='" + getHasSmartCard() + "'" +
            ", feesDescription='" + getFeesDescription() + "'" +
            ", active='" + getActive() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
