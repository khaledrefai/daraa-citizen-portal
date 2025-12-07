package gov.daraa.citizen.repository;

import gov.daraa.citizen.domain.RequiredDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RequiredDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, Long> {}
