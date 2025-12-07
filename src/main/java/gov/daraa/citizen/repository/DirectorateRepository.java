package gov.daraa.citizen.repository;

import gov.daraa.citizen.domain.Directorate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Directorate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectorateRepository extends JpaRepository<Directorate, Long> {}
