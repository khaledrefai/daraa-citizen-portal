package gov.daraa.citizen.service;

import gov.daraa.citizen.domain.CitizenService;
import gov.daraa.citizen.repository.CitizenServiceRepository;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.mapper.CitizenServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gov.daraa.citizen.domain.CitizenService}.
 */
@Service
@Transactional
public class CitizenServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(CitizenServiceService.class);

    private final CitizenServiceRepository citizenServiceRepository;

    private final CitizenServiceMapper citizenServiceMapper;

    public CitizenServiceService(CitizenServiceRepository citizenServiceRepository, CitizenServiceMapper citizenServiceMapper) {
        this.citizenServiceRepository = citizenServiceRepository;
        this.citizenServiceMapper = citizenServiceMapper;
    }

    /**
     * Save a citizenService.
     *
     * @param citizenServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public CitizenServiceDTO save(CitizenServiceDTO citizenServiceDTO) {
        LOG.debug("Request to save CitizenService : {}", citizenServiceDTO);
        CitizenService citizenService = citizenServiceMapper.toEntity(citizenServiceDTO);
        citizenService = citizenServiceRepository.save(citizenService);
        return citizenServiceMapper.toDto(citizenService);
    }

    /**
     * Update a citizenService.
     *
     * @param citizenServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public CitizenServiceDTO update(CitizenServiceDTO citizenServiceDTO) {
        LOG.debug("Request to update CitizenService : {}", citizenServiceDTO);
        CitizenService citizenService = citizenServiceMapper.toEntity(citizenServiceDTO);
        citizenService = citizenServiceRepository.save(citizenService);
        return citizenServiceMapper.toDto(citizenService);
    }

    /**
     * Partially update a citizenService.
     *
     * @param citizenServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CitizenServiceDTO> partialUpdate(CitizenServiceDTO citizenServiceDTO) {
        LOG.debug("Request to partially update CitizenService : {}", citizenServiceDTO);

        return citizenServiceRepository
            .findById(citizenServiceDTO.getId())
            .map(existingCitizenService -> {
                citizenServiceMapper.partialUpdate(existingCitizenService, citizenServiceDTO);

                return existingCitizenService;
            })
            .map(citizenServiceRepository::save)
            .map(citizenServiceMapper::toDto);
    }

    /**
     * Get all the citizenServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CitizenServiceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CitizenServices");
        return citizenServiceRepository.findAll(pageable).map(citizenServiceMapper::toDto);
    }

    /**
     * Get one citizenService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CitizenServiceDTO> findOne(Long id) {
        LOG.debug("Request to get CitizenService : {}", id);
        return citizenServiceRepository.findById(id).map(citizenServiceMapper::toDto);
    }

    /**
     * Delete the citizenService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CitizenService : {}", id);
        citizenServiceRepository.deleteById(id);
    }
}
