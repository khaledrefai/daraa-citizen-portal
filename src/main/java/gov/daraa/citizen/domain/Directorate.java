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
 * المديرية (مثلاً: مديرية المالية، مديرية الخدمات الفنية، مديرية الشؤون المدنية...)
 */
@Entity
@Table(name = "directorate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Directorate implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "directorate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "services", "directorate" }, allowSetters = true)
    private Set<ServiceCategory> serviceCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Directorate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Directorate code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Directorate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Directorate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Directorate active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ServiceCategory> getServiceCategories() {
        return this.serviceCategories;
    }

    public void setServiceCategories(Set<ServiceCategory> serviceCategories) {
        if (this.serviceCategories != null) {
            this.serviceCategories.forEach(i -> i.setDirectorate(null));
        }
        if (serviceCategories != null) {
            serviceCategories.forEach(i -> i.setDirectorate(this));
        }
        this.serviceCategories = serviceCategories;
    }

    public Directorate serviceCategories(Set<ServiceCategory> serviceCategories) {
        this.setServiceCategories(serviceCategories);
        return this;
    }

    public Directorate addServiceCategories(ServiceCategory serviceCategory) {
        this.serviceCategories.add(serviceCategory);
        serviceCategory.setDirectorate(this);
        return this;
    }

    public Directorate removeServiceCategories(ServiceCategory serviceCategory) {
        this.serviceCategories.remove(serviceCategory);
        serviceCategory.setDirectorate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Directorate)) {
            return false;
        }
        return getId() != null && getId().equals(((Directorate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Directorate{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
