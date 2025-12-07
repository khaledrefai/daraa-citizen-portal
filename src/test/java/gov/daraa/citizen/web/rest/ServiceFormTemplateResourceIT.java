package gov.daraa.citizen.web.rest;

import static gov.daraa.citizen.domain.ServiceFormTemplateAsserts.*;
import static gov.daraa.citizen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.daraa.citizen.IntegrationTest;
import gov.daraa.citizen.domain.ServiceFormTemplate;
import gov.daraa.citizen.repository.ServiceFormTemplateRepository;
import gov.daraa.citizen.service.dto.ServiceFormTemplateDTO;
import gov.daraa.citizen.service.mapper.ServiceFormTemplateMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link ServiceFormTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceFormTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_ORDER_INDEX = 0;
    private static final Integer UPDATED_ORDER_INDEX = 1;

    private static final String ENTITY_API_URL = "/api/service-form-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceFormTemplateRepository serviceFormTemplateRepository;

    @Autowired
    private ServiceFormTemplateMapper serviceFormTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceFormTemplateMockMvc;

    private ServiceFormTemplate serviceFormTemplate;

    private ServiceFormTemplate insertedServiceFormTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceFormTemplate createEntity() {
        return new ServiceFormTemplate()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .active(DEFAULT_ACTIVE)
            .orderIndex(DEFAULT_ORDER_INDEX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceFormTemplate createUpdatedEntity() {
        return new ServiceFormTemplate()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE)
            .orderIndex(UPDATED_ORDER_INDEX);
    }

    @BeforeEach
    void initTest() {
        serviceFormTemplate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceFormTemplate != null) {
            serviceFormTemplateRepository.delete(insertedServiceFormTemplate);
            insertedServiceFormTemplate = null;
        }
    }

    @Test
    @Transactional
    void createServiceFormTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);
        var returnedServiceFormTemplateDTO = om.readValue(
            restServiceFormTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceFormTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceFormTemplateDTO.class
        );

        // Validate the ServiceFormTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceFormTemplate = serviceFormTemplateMapper.toEntity(returnedServiceFormTemplateDTO);
        assertServiceFormTemplateUpdatableFieldsEquals(
            returnedServiceFormTemplate,
            getPersistedServiceFormTemplate(returnedServiceFormTemplate)
        );

        insertedServiceFormTemplate = returnedServiceFormTemplate;
    }

    @Test
    @Transactional
    void createServiceFormTemplateWithExistingId() throws Exception {
        // Create the ServiceFormTemplate with an existing ID
        serviceFormTemplate.setId(1L);
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceFormTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceFormTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceFormTemplate.setName(null);

        // Create the ServiceFormTemplate, which fails.
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        restServiceFormTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceFormTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceFormTemplate.setActive(null);

        // Create the ServiceFormTemplate, which fails.
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        restServiceFormTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceFormTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceFormTemplates() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        // Get all the serviceFormTemplateList
        restServiceFormTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFormTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)));
    }

    @Test
    @Transactional
    void getServiceFormTemplate() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        // Get the serviceFormTemplate
        restServiceFormTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceFormTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceFormTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingServiceFormTemplate() throws Exception {
        // Get the serviceFormTemplate
        restServiceFormTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceFormTemplate() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceFormTemplate
        ServiceFormTemplate updatedServiceFormTemplate = serviceFormTemplateRepository.findById(serviceFormTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceFormTemplate are not directly saved in db
        em.detach(updatedServiceFormTemplate);
        updatedServiceFormTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE)
            .orderIndex(UPDATED_ORDER_INDEX);
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(updatedServiceFormTemplate);

        restServiceFormTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceFormTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceFormTemplateToMatchAllProperties(updatedServiceFormTemplate);
    }

    @Test
    @Transactional
    void putNonExistingServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceFormTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceFormTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceFormTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceFormTemplate using partial update
        ServiceFormTemplate partialUpdatedServiceFormTemplate = new ServiceFormTemplate();
        partialUpdatedServiceFormTemplate.setId(serviceFormTemplate.getId());

        partialUpdatedServiceFormTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .orderIndex(UPDATED_ORDER_INDEX);

        restServiceFormTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceFormTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceFormTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ServiceFormTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceFormTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceFormTemplate, serviceFormTemplate),
            getPersistedServiceFormTemplate(serviceFormTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceFormTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceFormTemplate using partial update
        ServiceFormTemplate partialUpdatedServiceFormTemplate = new ServiceFormTemplate();
        partialUpdatedServiceFormTemplate.setId(serviceFormTemplate.getId());

        partialUpdatedServiceFormTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE)
            .orderIndex(UPDATED_ORDER_INDEX);

        restServiceFormTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceFormTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceFormTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ServiceFormTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceFormTemplateUpdatableFieldsEquals(
            partialUpdatedServiceFormTemplate,
            getPersistedServiceFormTemplate(partialUpdatedServiceFormTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceFormTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceFormTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceFormTemplate.setId(longCount.incrementAndGet());

        // Create the ServiceFormTemplate
        ServiceFormTemplateDTO serviceFormTemplateDTO = serviceFormTemplateMapper.toDto(serviceFormTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceFormTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceFormTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceFormTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceFormTemplate() throws Exception {
        // Initialize the database
        insertedServiceFormTemplate = serviceFormTemplateRepository.saveAndFlush(serviceFormTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceFormTemplate
        restServiceFormTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceFormTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceFormTemplateRepository.count();
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

    protected ServiceFormTemplate getPersistedServiceFormTemplate(ServiceFormTemplate serviceFormTemplate) {
        return serviceFormTemplateRepository.findById(serviceFormTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedServiceFormTemplateToMatchAllProperties(ServiceFormTemplate expectedServiceFormTemplate) {
        assertServiceFormTemplateAllPropertiesEquals(
            expectedServiceFormTemplate,
            getPersistedServiceFormTemplate(expectedServiceFormTemplate)
        );
    }

    protected void assertPersistedServiceFormTemplateToMatchUpdatableProperties(ServiceFormTemplate expectedServiceFormTemplate) {
        assertServiceFormTemplateAllUpdatablePropertiesEquals(
            expectedServiceFormTemplate,
            getPersistedServiceFormTemplate(expectedServiceFormTemplate)
        );
    }
}
