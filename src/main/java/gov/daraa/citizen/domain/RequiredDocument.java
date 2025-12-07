package gov.daraa.citizen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * الأوراق المطلوبة لكل خدمة
 */
@Entity
@Table(name = "required_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequiredDocument implements Serializable {

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

    @NotNull
    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory;

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

    public RequiredDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RequiredDocument name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public RequiredDocument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMandatory() {
        return this.mandatory;
    }

    public RequiredDocument mandatory(Boolean mandatory) {
        this.setMandatory(mandatory);
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public RequiredDocument orderIndex(Integer orderIndex) {
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

    public RequiredDocument service(CitizenService citizenService) {
        this.setService(citizenService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequiredDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((RequiredDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequiredDocument{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", orderIndex=" + getOrderIndex() +
            "}";
    }
}
