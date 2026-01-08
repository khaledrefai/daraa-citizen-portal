package gov.daraa.citizen.service;

import gov.daraa.citizen.domain.ServiceFormTemplate;
import gov.daraa.citizen.repository.ServiceFormTemplateRepository;
import gov.daraa.citizen.service.dto.ServiceFormTemplateDTO;
import gov.daraa.citizen.service.mapper.ServiceFormTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gov.daraa.citizen.domain.ServiceFormTemplate}.
 */
@Service
@Transactional
public class ServiceFormTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceFormTemplateService.class);

    private final ServiceFormTemplateRepository serviceFormTemplateRepository;

    private final ServiceFormTemplateMapper serviceFormTemplateMapper;

    public ServiceFormTemplateService(
        ServiceFormTemplateRepository serviceFormTemplateRepository,
        ServiceFormTemplateMapper serviceFormTemplateMapper
    ) {
        this.serviceFormTemplateRepository = serviceFormTemplateRepository;
        this.serviceFormTemplateMapper = serviceFormTemplateMapper;
    }

    /**
     * Save a serviceFormTemplate.
     *
     * @param serviceFormTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceFormTemplateDTO save(ServiceFormTemplateDTO serviceFormTemplateDTO) {
        LOG.debug("Request to save ServiceFormTemplate : {}", serviceFormTemplateDTO);
        ServiceFormTemplate serviceFormTemplate = serviceFormTemplateMapper.toEntity(serviceFormTemplateDTO);
        serviceFormTemplate = serviceFormTemplateRepository.save(serviceFormTemplate);
        return serviceFormTemplateMapper.toDto(serviceFormTemplate);
    }

    /**
     * Update a serviceFormTemplate.
     *
     * @param serviceFormTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceFormTemplateDTO update(ServiceFormTemplateDTO serviceFormTemplateDTO) {
        LOG.debug("Request to update ServiceFormTemplate : {}", serviceFormTemplateDTO);
        ServiceFormTemplate serviceFormTemplate = serviceFormTemplateMapper.toEntity(serviceFormTemplateDTO);
        serviceFormTemplate = serviceFormTemplateRepository.save(serviceFormTemplate);
        return serviceFormTemplateMapper.toDto(serviceFormTemplate);
    }

    /**
     * Partially update a serviceFormTemplate.
     *
     * @param serviceFormTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceFormTemplateDTO> partialUpdate(ServiceFormTemplateDTO serviceFormTemplateDTO) {
        LOG.debug("Request to partially update ServiceFormTemplate : {}", serviceFormTemplateDTO);

        return serviceFormTemplateRepository
            .findById(serviceFormTemplateDTO.getId())
            .map(existingServiceFormTemplate -> {
                serviceFormTemplateMapper.partialUpdate(existingServiceFormTemplate, serviceFormTemplateDTO);

                return existingServiceFormTemplate;
            })
            .map(serviceFormTemplateRepository::save)
            .map(serviceFormTemplateMapper::toDto);
    }

    /**
     * Get all the serviceFormTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceFormTemplateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceFormTemplates");
        return serviceFormTemplateRepository.findAll(pageable).map(serviceFormTemplateMapper::toDto);
    }

    /**
     * Get one serviceFormTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceFormTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceFormTemplate : {}", id);
        return serviceFormTemplateRepository.findById(id).map(serviceFormTemplateMapper::toDto);
    }

    /**
     * Delete the serviceFormTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceFormTemplate : {}", id);
        serviceFormTemplateRepository.deleteById(id);
    }
}
