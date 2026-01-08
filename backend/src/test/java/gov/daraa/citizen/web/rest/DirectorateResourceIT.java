package gov.daraa.citizen.web.rest;

import static gov.daraa.citizen.domain.DirectorateAsserts.*;
import static gov.daraa.citizen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.daraa.citizen.IntegrationTest;
import gov.daraa.citizen.domain.Directorate;
import gov.daraa.citizen.repository.DirectorateRepository;
import gov.daraa.citizen.service.dto.DirectorateDTO;
import gov.daraa.citizen.service.mapper.DirectorateMapper;
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
 * Integration tests for the {@link DirectorateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirectorateResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/directorates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DirectorateRepository directorateRepository;

    @Autowired
    private DirectorateMapper directorateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectorateMockMvc;

    private Directorate directorate;

    private Directorate insertedDirectorate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directorate createEntity() {
        return new Directorate().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directorate createUpdatedEntity() {
        return new Directorate().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        directorate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDirectorate != null) {
            directorateRepository.delete(insertedDirectorate);
            insertedDirectorate = null;
        }
    }

    @Test
    @Transactional
    void createDirectorate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);
        var returnedDirectorateDTO = om.readValue(
            restDirectorateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DirectorateDTO.class
        );

        // Validate the Directorate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDirectorate = directorateMapper.toEntity(returnedDirectorateDTO);
        assertDirectorateUpdatableFieldsEquals(returnedDirectorate, getPersistedDirectorate(returnedDirectorate));

        insertedDirectorate = returnedDirectorate;
    }

    @Test
    @Transactional
    void createDirectorateWithExistingId() throws Exception {
        // Create the Directorate with an existing ID
        directorate.setId(1L);
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directorate.setCode(null);

        // Create the Directorate, which fails.
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        restDirectorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directorate.setName(null);

        // Create the Directorate, which fails.
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        restDirectorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directorate.setActive(null);

        // Create the Directorate, which fails.
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        restDirectorateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirectorates() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        // Get all the directorateList
        restDirectorateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directorate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getDirectorate() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        // Get the directorate
        restDirectorateMockMvc
            .perform(get(ENTITY_API_URL_ID, directorate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directorate.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingDirectorate() throws Exception {
        // Get the directorate
        restDirectorateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDirectorate() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directorate
        Directorate updatedDirectorate = directorateRepository.findById(directorate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDirectorate are not directly saved in db
        em.detach(updatedDirectorate);
        updatedDirectorate.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);
        DirectorateDTO directorateDTO = directorateMapper.toDto(updatedDirectorate);

        restDirectorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directorateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directorateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDirectorateToMatchAllProperties(updatedDirectorate);
    }

    @Test
    @Transactional
    void putNonExistingDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directorateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirectorateWithPatch() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directorate using partial update
        Directorate partialUpdatedDirectorate = new Directorate();
        partialUpdatedDirectorate.setId(directorate.getId());

        partialUpdatedDirectorate.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restDirectorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectorate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectorate))
            )
            .andExpect(status().isOk());

        // Validate the Directorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectorateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDirectorate, directorate),
            getPersistedDirectorate(directorate)
        );
    }

    @Test
    @Transactional
    void fullUpdateDirectorateWithPatch() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directorate using partial update
        Directorate partialUpdatedDirectorate = new Directorate();
        partialUpdatedDirectorate.setId(directorate.getId());

        partialUpdatedDirectorate.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restDirectorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectorate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectorate))
            )
            .andExpect(status().isOk());

        // Validate the Directorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectorateUpdatableFieldsEquals(partialUpdatedDirectorate, getPersistedDirectorate(partialUpdatedDirectorate));
    }

    @Test
    @Transactional
    void patchNonExistingDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directorateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directorate.setId(longCount.incrementAndGet());

        // Create the Directorate
        DirectorateDTO directorateDTO = directorateMapper.toDto(directorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectorateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(directorateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirectorate() throws Exception {
        // Initialize the database
        insertedDirectorate = directorateRepository.saveAndFlush(directorate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the directorate
        restDirectorateMockMvc
            .perform(delete(ENTITY_API_URL_ID, directorate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return directorateRepository.count();
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

    protected Directorate getPersistedDirectorate(Directorate directorate) {
        return directorateRepository.findById(directorate.getId()).orElseThrow();
    }

    protected void assertPersistedDirectorateToMatchAllProperties(Directorate expectedDirectorate) {
        assertDirectorateAllPropertiesEquals(expectedDirectorate, getPersistedDirectorate(expectedDirectorate));
    }

    protected void assertPersistedDirectorateToMatchUpdatableProperties(Directorate expectedDirectorate) {
        assertDirectorateAllUpdatablePropertiesEquals(expectedDirectorate, getPersistedDirectorate(expectedDirectorate));
    }
}
