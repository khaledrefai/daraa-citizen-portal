package gov.daraa.citizen.repository;

import gov.daraa.citizen.domain.ServiceFormTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceFormTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceFormTemplateRepository extends JpaRepository<ServiceFormTemplate, Long> {}
