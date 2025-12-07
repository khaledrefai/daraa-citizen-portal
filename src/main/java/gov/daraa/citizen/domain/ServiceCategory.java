package gov.daraa.citizen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * تصنيف الخدمة (مثلاً: خدمات السجل المدني، خدمات البناء، خدمات الشكاوى...)
 * كل تصنيف تابع لمديرية واحدة.
 */
@Entity
@Table(name = "service_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceCategory implements Serializable {

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

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requiredDocuments", "formTemplates", "category" }, allowSetters = true)
    private Set<CitizenService> services = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "serviceCategories" }, allowSetters = true)
    private Directorate directorate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ServiceCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ServiceCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ServiceCategory active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<CitizenService> getServices() {
        return this.services;
    }

    public void setServices(Set<CitizenService> citizenServices) {
        if (this.services != null) {
            this.services.forEach(i -> i.setCategory(null));
        }
        if (citizenServices != null) {
            citizenServices.forEach(i -> i.setCategory(this));
        }
        this.services = citizenServices;
    }

    public ServiceCategory services(Set<CitizenService> citizenServices) {
        this.setServices(citizenServices);
        return this;
    }

    public ServiceCategory addServices(CitizenService citizenService) {
        this.services.add(citizenService);
        citizenService.setCategory(this);
        return this;
    }

    public ServiceCategory removeServices(CitizenService citizenService) {
        this.services.remove(citizenService);
        citizenService.setCategory(null);
        return this;
    }

    public Directorate getDirectorate() {
        return this.directorate;
    }

    public void setDirectorate(Directorate directorate) {
        this.directorate = directorate;
    }

    public ServiceCategory directorate(Directorate directorate) {
        this.setDirectorate(directorate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceCategory{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
