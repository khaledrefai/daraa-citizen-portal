package gov.daraa.citizen.web.rest;

import static gov.daraa.citizen.domain.CitizenServiceAsserts.*;
import static gov.daraa.citizen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.daraa.citizen.IntegrationTest;
import gov.daraa.citizen.domain.CitizenService;
import gov.daraa.citizen.domain.enumeration.EstimatedTimeUnit;
import gov.daraa.citizen.repository.CitizenServiceRepository;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.mapper.CitizenServiceMapper;
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
 * Integration tests for the {@link CitizenServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitizenServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTIMATED_DURATION = 1;
    private static final Integer UPDATED_ESTIMATED_DURATION = 2;

    private static final EstimatedTimeUnit DEFAULT_ESTIMATED_DURATION_UNIT = EstimatedTimeUnit.MINUTE;
    private static final EstimatedTimeUnit UPDATED_ESTIMATED_DURATION_UNIT = EstimatedTimeUnit.HOUR;

    private static final Boolean DEFAULT_REQUIRES_PHYSICAL_PRESENCE = false;
    private static final Boolean UPDATED_REQUIRES_PHYSICAL_PRESENCE = true;

    private static final Boolean DEFAULT_IS_ELECTRONIC = false;
    private static final Boolean UPDATED_IS_ELECTRONIC = true;

    private static final Boolean DEFAULT_HAS_SMART_CARD = false;
    private static final Boolean UPDATED_HAS_SMART_CARD = true;

    private static final String DEFAULT_FEES_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FEES_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/citizen-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitizenServiceRepository citizenServiceRepository;

    @Autowired
    private CitizenServiceMapper citizenServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitizenServiceMockMvc;

    private CitizenService citizenService;

    private CitizenService insertedCitizenService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitizenService createEntity() {
        return new CitizenService()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .estimatedDuration(DEFAULT_ESTIMATED_DURATION)
            .estimatedDurationUnit(DEFAULT_ESTIMATED_DURATION_UNIT)
            .requiresPhysicalPresence(DEFAULT_REQUIRES_PHYSICAL_PRESENCE)
            .isElectronic(DEFAULT_IS_ELECTRONIC)
            .hasSmartCard(DEFAULT_HAS_SMART_CARD)
            .feesDescription(DEFAULT_FEES_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitizenService createUpdatedEntity() {
        return new CitizenService()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .estimatedDurationUnit(UPDATED_ESTIMATED_DURATION_UNIT)
            .requiresPhysicalPresence(UPDATED_REQUIRES_PHYSICAL_PRESENCE)
            .isElectronic(UPDATED_IS_ELECTRONIC)
            .hasSmartCard(UPDATED_HAS_SMART_CARD)
            .feesDescription(UPDATED_FEES_DESCRIPTION)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        citizenService = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCitizenService != null) {
            citizenServiceRepository.delete(insertedCitizenService);
            insertedCitizenService = null;
        }
    }

    @Test
    @Transactional
    void createCitizenService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);
        var returnedCitizenServiceDTO = om.readValue(
            restCitizenServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CitizenServiceDTO.class
        );

        // Validate the CitizenService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCitizenService = citizenServiceMapper.toEntity(returnedCitizenServiceDTO);
        assertCitizenServiceUpdatableFieldsEquals(returnedCitizenService, getPersistedCitizenService(returnedCitizenService));

        insertedCitizenService = returnedCitizenService;
    }

    @Test
    @Transactional
    void createCitizenServiceWithExistingId() throws Exception {
        // Create the CitizenService with an existing ID
        citizenService.setId(1L);
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setCode(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setName(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstimatedDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setEstimatedDuration(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstimatedDurationUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setEstimatedDurationUnit(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiresPhysicalPresenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setRequiresPhysicalPresence(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsElectronicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setIsElectronic(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasSmartCardIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setHasSmartCard(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenService.setActive(null);

        // Create the CitizenService, which fails.
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        restCitizenServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCitizenServices() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        // Get all the citizenServiceList
        restCitizenServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citizenService.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].estimatedDuration").value(hasItem(DEFAULT_ESTIMATED_DURATION)))
            .andExpect(jsonPath("$.[*].estimatedDurationUnit").value(hasItem(DEFAULT_ESTIMATED_DURATION_UNIT.toString())))
            .andExpect(jsonPath("$.[*].requiresPhysicalPresence").value(hasItem(DEFAULT_REQUIRES_PHYSICAL_PRESENCE)))
            .andExpect(jsonPath("$.[*].isElectronic").value(hasItem(DEFAULT_IS_ELECTRONIC)))
            .andExpect(jsonPath("$.[*].hasSmartCard").value(hasItem(DEFAULT_HAS_SMART_CARD)))
            .andExpect(jsonPath("$.[*].feesDescription").value(hasItem(DEFAULT_FEES_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getCitizenService() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        // Get the citizenService
        restCitizenServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, citizenService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(citizenService.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.estimatedDuration").value(DEFAULT_ESTIMATED_DURATION))
            .andExpect(jsonPath("$.estimatedDurationUnit").value(DEFAULT_ESTIMATED_DURATION_UNIT.toString()))
            .andExpect(jsonPath("$.requiresPhysicalPresence").value(DEFAULT_REQUIRES_PHYSICAL_PRESENCE))
            .andExpect(jsonPath("$.isElectronic").value(DEFAULT_IS_ELECTRONIC))
            .andExpect(jsonPath("$.hasSmartCard").value(DEFAULT_HAS_SMART_CARD))
            .andExpect(jsonPath("$.feesDescription").value(DEFAULT_FEES_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingCitizenService() throws Exception {
        // Get the citizenService
        restCitizenServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCitizenService() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenService
        CitizenService updatedCitizenService = citizenServiceRepository.findById(citizenService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCitizenService are not directly saved in db
        em.detach(updatedCitizenService);
        updatedCitizenService
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .estimatedDurationUnit(UPDATED_ESTIMATED_DURATION_UNIT)
            .requiresPhysicalPresence(UPDATED_REQUIRES_PHYSICAL_PRESENCE)
            .isElectronic(UPDATED_IS_ELECTRONIC)
            .hasSmartCard(UPDATED_HAS_SMART_CARD)
            .feesDescription(UPDATED_FEES_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(updatedCitizenService);

        restCitizenServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citizenServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citizenServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitizenServiceToMatchAllProperties(updatedCitizenService);
    }

    @Test
    @Transactional
    void putNonExistingCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citizenServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citizenServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citizenServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitizenServiceWithPatch() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenService using partial update
        CitizenService partialUpdatedCitizenService = new CitizenService();
        partialUpdatedCitizenService.setId(citizenService.getId());

        partialUpdatedCitizenService
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .requiresPhysicalPresence(UPDATED_REQUIRES_PHYSICAL_PRESENCE)
            .hasSmartCard(UPDATED_HAS_SMART_CARD)
            .active(UPDATED_ACTIVE);

        restCitizenServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitizenService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitizenService))
            )
            .andExpect(status().isOk());

        // Validate the CitizenService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitizenServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCitizenService, citizenService),
            getPersistedCitizenService(citizenService)
        );
    }

    @Test
    @Transactional
    void fullUpdateCitizenServiceWithPatch() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenService using partial update
        CitizenService partialUpdatedCitizenService = new CitizenService();
        partialUpdatedCitizenService.setId(citizenService.getId());

        partialUpdatedCitizenService
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .estimatedDurationUnit(UPDATED_ESTIMATED_DURATION_UNIT)
            .requiresPhysicalPresence(UPDATED_REQUIRES_PHYSICAL_PRESENCE)
            .isElectronic(UPDATED_IS_ELECTRONIC)
            .hasSmartCard(UPDATED_HAS_SMART_CARD)
            .feesDescription(UPDATED_FEES_DESCRIPTION)
            .active(UPDATED_ACTIVE);

        restCitizenServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitizenService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitizenService))
            )
            .andExpect(status().isOk());

        // Validate the CitizenService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitizenServiceUpdatableFieldsEquals(partialUpdatedCitizenService, getPersistedCitizenService(partialUpdatedCitizenService));
    }

    @Test
    @Transactional
    void patchNonExistingCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citizenServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citizenServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citizenServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCitizenService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenService.setId(longCount.incrementAndGet());

        // Create the CitizenService
        CitizenServiceDTO citizenServiceDTO = citizenServiceMapper.toDto(citizenService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citizenServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitizenService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCitizenService() throws Exception {
        // Initialize the database
        insertedCitizenService = citizenServiceRepository.saveAndFlush(citizenService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the citizenService
        restCitizenServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, citizenService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citizenServiceRepository.count();
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

    protected CitizenService getPersistedCitizenService(CitizenService citizenService) {
        return citizenServiceRepository.findById(citizenService.getId()).orElseThrow();
    }

    protected void assertPersistedCitizenServiceToMatchAllProperties(CitizenService expectedCitizenService) {
        assertCitizenServiceAllPropertiesEquals(expectedCitizenService, getPersistedCitizenService(expectedCitizenService));
    }

    protected void assertPersistedCitizenServiceToMatchUpdatableProperties(CitizenService expectedCitizenService) {
        assertCitizenServiceAllUpdatablePropertiesEquals(expectedCitizenService, getPersistedCitizenService(expectedCitizenService));
    }
}
