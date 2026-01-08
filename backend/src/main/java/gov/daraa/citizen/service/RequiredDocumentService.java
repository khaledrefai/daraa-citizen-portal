package gov.daraa.citizen.service;

import gov.daraa.citizen.domain.RequiredDocument;
import gov.daraa.citizen.repository.RequiredDocumentRepository;
import gov.daraa.citizen.service.dto.RequiredDocumentDTO;
import gov.daraa.citizen.service.mapper.RequiredDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gov.daraa.citizen.domain.RequiredDocument}.
 */
@Service
@Transactional
public class RequiredDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(RequiredDocumentService.class);

    private final RequiredDocumentRepository requiredDocumentRepository;

    private final RequiredDocumentMapper requiredDocumentMapper;

    public RequiredDocumentService(RequiredDocumentRepository requiredDocumentRepository, RequiredDocumentMapper requiredDocumentMapper) {
        this.requiredDocumentRepository = requiredDocumentRepository;
        this.requiredDocumentMapper = requiredDocumentMapper;
    }

    /**
     * Save a requiredDocument.
     *
     * @param requiredDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public RequiredDocumentDTO save(RequiredDocumentDTO requiredDocumentDTO) {
        LOG.debug("Request to save RequiredDocument : {}", requiredDocumentDTO);
        RequiredDocument requiredDocument = requiredDocumentMapper.toEntity(requiredDocumentDTO);
        requiredDocument = requiredDocumentRepository.save(requiredDocument);
        return requiredDocumentMapper.toDto(requiredDocument);
    }

    /**
     * Update a requiredDocument.
     *
     * @param requiredDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public RequiredDocumentDTO update(RequiredDocumentDTO requiredDocumentDTO) {
        LOG.debug("Request to update RequiredDocument : {}", requiredDocumentDTO);
        RequiredDocument requiredDocument = requiredDocumentMapper.toEntity(requiredDocumentDTO);
        requiredDocument = requiredDocumentRepository.save(requiredDocument);
        return requiredDocumentMapper.toDto(requiredDocument);
    }

    /**
     * Partially update a requiredDocument.
     *
     * @param requiredDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RequiredDocumentDTO> partialUpdate(RequiredDocumentDTO requiredDocumentDTO) {
        LOG.debug("Request to partially update RequiredDocument : {}", requiredDocumentDTO);

        return requiredDocumentRepository
            .findById(requiredDocumentDTO.getId())
            .map(existingRequiredDocument -> {
                requiredDocumentMapper.partialUpdate(existingRequiredDocument, requiredDocumentDTO);

                return existingRequiredDocument;
            })
            .map(requiredDocumentRepository::save)
            .map(requiredDocumentMapper::toDto);
    }

    /**
     * Get all the requiredDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RequiredDocumentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RequiredDocuments");
        return requiredDocumentRepository.findAll(pageable).map(requiredDocumentMapper::toDto);
    }

    /**
     * Get one requiredDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RequiredDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get RequiredDocument : {}", id);
        return requiredDocumentRepository.findById(id).map(requiredDocumentMapper::toDto);
    }

    /**
     * Delete the requiredDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RequiredDocument : {}", id);
        requiredDocumentRepository.deleteById(id);
    }
}
