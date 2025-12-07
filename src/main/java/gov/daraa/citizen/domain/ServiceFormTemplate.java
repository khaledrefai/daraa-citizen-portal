package gov.daraa.citizen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * نموذج مرفق مرتبط بالخدمة
 * يستخدم ليقوم المواطن بتنزيل النموذج وتعبئته ثم رفعه مرة أخرى.
 */
@Entity
@Table(name = "service_form_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceFormTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;

    @NotNull
    @Column(name = "file_content_type", nullable = false)
    private String fileContentType;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Min(value = 0)
    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "requiredDocuments", "formTemplates", "category" }, allowSetters = true)
    private CitizenService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceFormTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServiceFormTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceFormTemplate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile() {
        return this.file;
    }

    public ServiceFormTemplate file(byte[] file) {
        this.setFile(file);
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public ServiceFormTemplate fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ServiceFormTemplate active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public ServiceFormTemplate orderIndex(Integer orderIndex) {
        this.setOrderIndex(orderIndex);
        return this;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public CitizenService getService() {
        return this.service;
    }

    public void setService(CitizenService citizenService) {
        this.service = citizenService;
    }

    public ServiceFormTemplate service(CitizenService citizenService) {
        this.setService(citizenService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceFormTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceFormTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceFormTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", active='" + getActive() + "'" +
            ", orderIndex=" + getOrderIndex() +
            "}";
    }
}
