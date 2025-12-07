package gov.daraa.citizen.web.rest;

import static gov.daraa.citizen.domain.ServiceCategoryAsserts.*;
import static gov.daraa.citizen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.daraa.citizen.IntegrationTest;
import gov.daraa.citizen.domain.ServiceCategory;
import gov.daraa.citizen.repository.ServiceCategoryRepository;
import gov.daraa.citizen.service.dto.ServiceCategoryDTO;
import gov.daraa.citizen.service.mapper.ServiceCategoryMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/service-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    private ServiceCategoryMapper serviceCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceCategoryMockMvc;

    private ServiceCategory serviceCategory;

    private ServiceCategory insertedServiceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createEntity() {
        return new ServiceCategory().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createUpdatedEntity() {
        return new ServiceCategory().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        serviceCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceCategory != null) {
            serviceCategoryRepository.delete(insertedServiceCategory);
            insertedServiceCategory = null;
        }
    }

    @Test
    @Transactional
    void createServiceCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);
        var returnedServiceCategoryDTO = om.readValue(
            restServiceCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceCategoryDTO.class
        );

        // Validate the ServiceCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceCategory = serviceCategoryMapper.toEntity(returnedServiceCategoryDTO);
        assertServiceCategoryUpdatableFieldsEquals(returnedServiceCategory, getPersistedServiceCategory(returnedServiceCategory));

        insertedServiceCategory = returnedServiceCategory;
    }

    @Test
    @Transactional
    void createServiceCategoryWithExistingId() throws Exception {
        // Create the ServiceCategory with an existing ID
        serviceCategory.setId(1L);
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceCategory.setCode(null);

        // Create the ServiceCategory, which fails.
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        restServiceCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceCategory.setName(null);

        // Create the ServiceCategory, which fails.
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        restServiceCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceCategory.setActive(null);

        // Create the ServiceCategory, which fails.
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        restServiceCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceCategories() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get all the serviceCategoryList
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getServiceCategory() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get the serviceCategory
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingServiceCategory() throws Exception {
        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceCategory() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceCategory
        ServiceCategory updatedServiceCategory = serviceCategoryRepository.findById(serviceCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceCategory are not directly saved in db
        em.detach(updatedServiceCategory);
        updatedServiceCategory.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(updatedServiceCategory);

        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceCategoryToMatchAllProperties(updatedServiceCategory);
    }

    @Test
    @Transactional
    void putNonExistingServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        partialUpdatedServiceCategory.description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceCategory, serviceCategory),
            getPersistedServiceCategory(serviceCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        partialUpdatedServiceCategory.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceCategoryUpdatableFieldsEquals(
            partialUpdatedServiceCategory,
            getPersistedServiceCategory(partialUpdatedServiceCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceCategory.setId(longCount.incrementAndGet());

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.toDto(serviceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceCategory() throws Exception {
        // Initialize the database
        insertedServiceCategory = serviceCategoryRepository.saveAndFlush(serviceCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceCategory
        restServiceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceCategoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ServiceCategory getPersistedServiceCategory(ServiceCategory serviceCategory) {
        return serviceCategoryRepository.findById(serviceCategory.getId()).orElseThrow();
    }

    protected void assertPersistedServiceCategoryToMatchAllProperties(ServiceCategory expectedServiceCategory) {
        assertServiceCategoryAllPropertiesEquals(expectedServiceCategory, getPersistedServiceCategory(expectedServiceCategory));
    }

    protected void assertPersistedServiceCategoryToMatchUpdatableProperties(ServiceCategory expectedServiceCategory) {
        assertServiceCategoryAllUpdatablePropertiesEquals(expectedServiceCategory, getPersistedServiceCategory(expectedServiceCategory));
    }
}
