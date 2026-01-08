package gov.daraa.citizen.service;

import gov.daraa.citizen.domain.ServiceCategory;
import gov.daraa.citizen.repository.ServiceCategoryRepository;
import gov.daraa.citizen.service.dto.ServiceCategoryDTO;
import gov.daraa.citizen.service.mapper.ServiceCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gov.daraa.citizen.domain.ServiceCategory}.
 */
@Service
@Transactional
public class ServiceCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceCategoryService.class);

    private final ServiceCategoryRepository serviceCategoryRepository;

    private final ServiceCategoryMapper serviceCategoryMapper;

    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository, ServiceCategoryMapper serviceCategoryMapper) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.serviceCategoryMapper = serviceCategoryMapper;
    }

    /**
     * Save a serviceCategory.
     *
     * @param serviceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceCategoryDTO save(ServiceCategoryDTO serviceCategoryDTO) {
        LOG.debug("Request to save ServiceCategory : {}", serviceCategoryDTO);
        ServiceCategory serviceCategory = serviceCategoryMapper.toEntity(serviceCategoryDTO);
        serviceCategory = serviceCategoryRepository.save(serviceCategory);
        return serviceCategoryMapper.toDto(serviceCategory);
    }

    /**
     * Update a serviceCategory.
     *
     * @param serviceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceCategoryDTO update(ServiceCategoryDTO serviceCategoryDTO) {
        LOG.debug("Request to update ServiceCategory : {}", serviceCategoryDTO);
        ServiceCategory serviceCategory = serviceCategoryMapper.toEntity(serviceCategoryDTO);
        serviceCategory = serviceCategoryRepository.save(serviceCategory);
        return serviceCategoryMapper.toDto(serviceCategory);
    }

    /**
     * Partially update a serviceCategory.
     *
     * @param serviceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceCategoryDTO> partialUpdate(ServiceCategoryDTO serviceCategoryDTO) {
        LOG.debug("Request to partially update ServiceCategory : {}", serviceCategoryDTO);

        return serviceCategoryRepository
            .findById(serviceCategoryDTO.getId())
            .map(existingServiceCategory -> {
                serviceCategoryMapper.partialUpdate(existingServiceCategory, serviceCategoryDTO);

                return existingServiceCategory;
            })
            .map(serviceCategoryRepository::save)
            .map(serviceCategoryMapper::toDto);
    }

    /**
     * Get all the serviceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceCategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceCategories");
        return serviceCategoryRepository.findAll(pageable).map(serviceCategoryMapper::toDto);
    }

    /**
     * Get one serviceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceCategory : {}", id);
        return serviceCategoryRepository.findById(id).map(serviceCategoryMapper::toDto);
    }

    /**
     * Delete the serviceCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceCategory : {}", id);
        serviceCategoryRepository.deleteById(id);
    }
}
