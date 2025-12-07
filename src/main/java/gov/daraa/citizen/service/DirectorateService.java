package gov.daraa.citizen.service;

import gov.daraa.citizen.domain.Directorate;
import gov.daraa.citizen.repository.DirectorateRepository;
import gov.daraa.citizen.service.dto.DirectorateDTO;
import gov.daraa.citizen.service.mapper.DirectorateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gov.daraa.citizen.domain.Directorate}.
 */
@Service
@Transactional
public class DirectorateService {

    private static final Logger LOG = LoggerFactory.getLogger(DirectorateService.class);

    private final DirectorateRepository directorateRepository;

    private final DirectorateMapper directorateMapper;

    public DirectorateService(DirectorateRepository directorateRepository, DirectorateMapper directorateMapper) {
        this.directorateRepository = directorateRepository;
        this.directorateMapper = directorateMapper;
    }

    /**
     * Save a directorate.
     *
     * @param directorateDTO the entity to save.
     * @return the persisted entity.
     */
    public DirectorateDTO save(DirectorateDTO directorateDTO) {
        LOG.debug("Request to save Directorate : {}", directorateDTO);
        Directorate directorate = directorateMapper.toEntity(directorateDTO);
        directorate = directorateRepository.save(directorate);
        return directorateMapper.toDto(directorate);
    }

    /**
     * Update a directorate.
     *
     * @param directorateDTO the entity to save.
     * @return the persisted entity.
     */
    public DirectorateDTO update(DirectorateDTO directorateDTO) {
        LOG.debug("Request to update Directorate : {}", directorateDTO);
        Directorate directorate = directorateMapper.toEntity(directorateDTO);
        directorate = directorateRepository.save(directorate);
        return directorateMapper.toDto(directorate);
    }

    /**
     * Partially update a directorate.
     *
     * @param directorateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DirectorateDTO> partialUpdate(DirectorateDTO directorateDTO) {
        LOG.debug("Request to partially update Directorate : {}", directorateDTO);

        return directorateRepository
            .findById(directorateDTO.getId())
            .map(existingDirectorate -> {
                directorateMapper.partialUpdate(existingDirectorate, directorateDTO);

                return existingDirectorate;
            })
            .map(directorateRepository::save)
            .map(directorateMapper::toDto);
    }

    /**
     * Get all the directorates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DirectorateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Directorates");
        return directorateRepository.findAll(pageable).map(directorateMapper::toDto);
    }

    /**
     * Get one directorate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DirectorateDTO> findOne(Long id) {
        LOG.debug("Request to get Directorate : {}", id);
        return directorateRepository.findById(id).map(directorateMapper::toDto);
    }

    /**
     * Delete the directorate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Directorate : {}", id);
        directorateRepository.deleteById(id);
    }
}
