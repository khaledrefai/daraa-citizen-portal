package gov.daraa.citizen.repository;

import gov.daraa.citizen.domain.CitizenService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CitizenService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitizenServiceRepository extends JpaRepository<CitizenService, Long> {}
