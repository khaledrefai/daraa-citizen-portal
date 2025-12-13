package gov.daraa.citizen.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link gov.daraa.citizen.domain.ServiceFormTemplate} entity.
 */
@Schema(description = "نموذج مرفق مرتبط بالخدمة\nيستخدم ليقوم المواطن بتنزيل النموذج وتعبئته ثم رفعه مرة أخرى.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceFormTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @Lob
    @NotNull
    private byte[] file;

    @NotNull
    private String fileContentType;

    @NotNull
    private Boolean active;

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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof ServiceFormTemplateDTO)) {
            return false;
        }

        ServiceFormTemplateDTO serviceFormTemplateDTO = (ServiceFormTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceFormTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceFormTemplateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", file='" + getFile() + "'" +
            ", active='" + getActive() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", service=" + getService() +
            "}";
    }
}
