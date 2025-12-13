package gov.daraa.citizen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.daraa.citizen.domain.enumeration.EstimatedTimeUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * خدمة المواطن
 * تحتوي على التصنيف، الوصف، الزمن المقدر، الأوراق المطلوبة،
 * وهل هي إلكترونية، وهل تتطلب حضور شخصي، وهل لها نموذج (كرتوني/ورقي) مرفق.
 */
@Entity
@Table(name = "citizen_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitizenService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Min(value = 1)
    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estimated_duration_unit", nullable = false)
    private EstimatedTimeUnit estimatedDurationUnit;

    @NotNull
    @Column(name = "requires_physical_presence", nullable = false)
    private Boolean requiresPhysicalPresence;

    @NotNull
    @Column(name = "is_electronic", nullable = false)
    private Boolean isElectronic;

    @NotNull
    @Column(name = "has_smart_card", nullable = false)
    private Boolean hasSmartCard;

    @Size(max = 1000)
    @Column(name = "fees_description", length = 1000)
    private String feesDescription;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private Set<RequiredDocument> requiredDocuments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private Set<ServiceFormTemplate> formTemplates = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "services", "directorate" }, allowSetters = true)
    private ServiceCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CitizenService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CitizenService code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public CitizenService name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CitizenService description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedDuration() {
        return this.estimatedDuration;
    }

    public CitizenService estimatedDuration(Integer estimatedDuration) {
        this.setEstimatedDuration(estimatedDuration);
        return this;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public EstimatedTimeUnit getEstimatedDurationUnit() {
        return this.estimatedDurationUnit;
    }

    public CitizenService estimatedDurationUnit(EstimatedTimeUnit estimatedDurationUnit) {
        this.setEstimatedDurationUnit(estimatedDurationUnit);
        return this;
    }

    public void setEstimatedDurationUnit(EstimatedTimeUnit estimatedDurationUnit) {
        this.estimatedDurationUnit = estimatedDurationUnit;
    }

    public Boolean getRequiresPhysicalPresence() {
        return this.requiresPhysicalPresence;
    }

    public CitizenService requiresPhysicalPresence(Boolean requiresPhysicalPresence) {
        this.setRequiresPhysicalPresence(requiresPhysicalPresence);
        return this;
    }

    public void setRequiresPhysicalPresence(Boolean requiresPhysicalPresence) {
        this.requiresPhysicalPresence = requiresPhysicalPresence;
    }

    public Boolean getIsElectronic() {
        return this.isElectronic;
    }

    public CitizenService isElectronic(Boolean isElectronic) {
        this.setIsElectronic(isElectronic);
        return this;
    }

    public void setIsElectronic(Boolean isElectronic) {
        this.isElectronic = isElectronic;
    }

    public Boolean getHasSmartCard() {
        return this.hasSmartCard;
    }

    public CitizenService hasSmartCard(Boolean hasSmartCard) {
        this.setHasSmartCard(hasSmartCard);
        return this;
    }

    public void setHasSmartCard(Boolean hasSmartCard) {
        this.hasSmartCard = hasSmartCard;
    }

    public String getFeesDescription() {
        return this.feesDescription;
    }

    public CitizenService feesDescription(String feesDescription) {
        this.setFeesDescription(feesDescription);
        return this;
    }

    public void setFeesDescription(String feesDescription) {
        this.feesDescription = feesDescription;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CitizenService active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<RequiredDocument> getRequiredDocuments() {
        return this.requiredDocuments;
    }

    public void setRequiredDocuments(Set<RequiredDocument> requiredDocuments) {
        if (this.requiredDocuments != null) {
            this.requiredDocuments.forEach(i -> i.setService(null));
        }
        if (requiredDocuments != null) {
            requiredDocuments.forEach(i -> i.setService(this));
        }
        this.requiredDocuments = requiredDocuments;
    }

    public CitizenService requiredDocuments(Set<RequiredDocument> requiredDocuments) {
        this.setRequiredDocuments(requiredDocuments);
        return this;
    }

    public CitizenService addRequiredDocuments(RequiredDocument requiredDocument) {
        this.requiredDocuments.add(requiredDocument);
        requiredDocument.setService(this);
        return this;
    }

    public CitizenService removeRequiredDocuments(RequiredDocument requiredDocument) {
        this.requiredDocuments.remove(requiredDocument);
        requiredDocument.setService(null);
        return this;
    }

    public Set<ServiceFormTemplate> getFormTemplates() {
        return this.formTemplates;
    }

    public void setFormTemplates(Set<ServiceFormTemplate> serviceFormTemplates) {
        if (this.formTemplates != null) {
            this.formTemplates.forEach(i -> i.setService(null));
        }
        if (serviceFormTemplates != null) {
            serviceFormTemplates.forEach(i -> i.setService(this));
        }
        this.formTemplates = serviceFormTemplates;
    }

    public CitizenService formTemplates(Set<ServiceFormTemplate> serviceFormTemplates) {
        this.setFormTemplates(serviceFormTemplates);
        return this;
    }

    public CitizenService addFormTemplates(ServiceFormTemplate serviceFormTemplate) {
        this.formTemplates.add(serviceFormTemplate);
        serviceFormTemplate.setService(this);
        return this;
    }

    public CitizenService removeFormTemplates(ServiceFormTemplate serviceFormTemplate) {
        this.formTemplates.remove(serviceFormTemplate);
        serviceFormTemplate.setService(null);
        return this;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public CitizenService category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitizenService)) {
            return false;
        }
        return getId() != null && getId().equals(((CitizenService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitizenService{" +
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
            "}";
    }
}
