package gov.daraa.citizen.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link gov.daraa.citizen.domain.ServiceCategory} entity.
 */
@Schema(description = "تصنيف الخدمة (مثلاً: خدمات السجل المدني، خدمات البناء، خدمات الشكاوى...)\nكل تصنيف تابع لمديرية واحدة.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String code;

    @NotNull
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Boolean active;

    private DirectorateDTO directorate;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DirectorateDTO getDirectorate() {
        return directorate;
    }

    public void setDirectorate(DirectorateDTO directorate) {
        this.directorate = directorate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceCategoryDTO)) {
            return false;
        }

        ServiceCategoryDTO serviceCategoryDTO = (ServiceCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceCategoryDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            ", directorate=" + getDirectorate() +
            "}";
    }
}
