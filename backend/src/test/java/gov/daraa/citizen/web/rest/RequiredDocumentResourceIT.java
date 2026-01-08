package gov.daraa.citizen.web.rest;

import static gov.daraa.citizen.domain.RequiredDocumentAsserts.*;
import static gov.daraa.citizen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.daraa.citizen.IntegrationTest;
import gov.daraa.citizen.domain.RequiredDocument;
import gov.daraa.citizen.repository.RequiredDocumentRepository;
import gov.daraa.citizen.service.dto.RequiredDocumentDTO;
import gov.daraa.citizen.service.mapper.RequiredDocumentMapper;
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
 * Integration tests for the {@link RequiredDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequiredDocumentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final Integer DEFAULT_ORDER_INDEX = 0;
    private static final Integer UPDATED_ORDER_INDEX = 1;

    private static final String ENTITY_API_URL = "/api/required-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private RequiredDocumentMapper requiredDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequiredDocumentMockMvc;

    private RequiredDocument requiredDocument;

    private RequiredDocument insertedRequiredDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequiredDocument createEntity() {
        return new RequiredDocument()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mandatory(DEFAULT_MANDATORY)
            .orderIndex(DEFAULT_ORDER_INDEX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequiredDocument createUpdatedEntity() {
        return new RequiredDocument()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mandatory(UPDATED_MANDATORY)
            .orderIndex(UPDATED_ORDER_INDEX);
    }

    @BeforeEach
    void initTest() {
        requiredDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRequiredDocument != null) {
            requiredDocumentRepository.delete(insertedRequiredDocument);
            insertedRequiredDocument = null;
        }
    }

    @Test
    @Transactional
    void createRequiredDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);
        var returnedRequiredDocumentDTO = om.readValue(
            restRequiredDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requiredDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RequiredDocumentDTO.class
        );

        // Validate the RequiredDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRequiredDocument = requiredDocumentMapper.toEntity(returnedRequiredDocumentDTO);
        assertRequiredDocumentUpdatableFieldsEquals(returnedRequiredDocument, getPersistedRequiredDocument(returnedRequiredDocument));

        insertedRequiredDocument = returnedRequiredDocument;
    }

    @Test
    @Transactional
    void createRequiredDocumentWithExistingId() throws Exception {
        // Create the RequiredDocument with an existing ID
        requiredDocument.setId(1L);
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequiredDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        requiredDocument.setName(null);

        // Create the RequiredDocument, which fails.
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        restRequiredDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMandatoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        requiredDocument.setMandatory(null);

        // Create the RequiredDocument, which fails.
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        restRequiredDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequiredDocuments() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        // Get all the requiredDocumentList
        restRequiredDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requiredDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY)))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)));
    }

    @Test
    @Transactional
    void getRequiredDocument() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        // Get the requiredDocument
        restRequiredDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, requiredDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requiredDocument.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingRequiredDocument() throws Exception {
        // Get the requiredDocument
        restRequiredDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRequiredDocument() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requiredDocument
        RequiredDocument updatedRequiredDocument = requiredDocumentRepository.findById(requiredDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRequiredDocument are not directly saved in db
        em.detach(updatedRequiredDocument);
        updatedRequiredDocument
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mandatory(UPDATED_MANDATORY)
            .orderIndex(UPDATED_ORDER_INDEX);
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(updatedRequiredDocument);

        restRequiredDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requiredDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requiredDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRequiredDocumentToMatchAllProperties(updatedRequiredDocument);
    }

    @Test
    @Transactional
    void putNonExistingRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requiredDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requiredDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requiredDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requiredDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequiredDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requiredDocument using partial update
        RequiredDocument partialUpdatedRequiredDocument = new RequiredDocument();
        partialUpdatedRequiredDocument.setId(requiredDocument.getId());

        partialUpdatedRequiredDocument.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRequiredDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequiredDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequiredDocument))
            )
            .andExpect(status().isOk());

        // Validate the RequiredDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequiredDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRequiredDocument, requiredDocument),
            getPersistedRequiredDocument(requiredDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateRequiredDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requiredDocument using partial update
        RequiredDocument partialUpdatedRequiredDocument = new RequiredDocument();
        partialUpdatedRequiredDocument.setId(requiredDocument.getId());

        partialUpdatedRequiredDocument
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mandatory(UPDATED_MANDATORY)
            .orderIndex(UPDATED_ORDER_INDEX);

        restRequiredDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequiredDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequiredDocument))
            )
            .andExpect(status().isOk());

        // Validate the RequiredDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequiredDocumentUpdatableFieldsEquals(
            partialUpdatedRequiredDocument,
            getPersistedRequiredDocument(partialUpdatedRequiredDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requiredDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requiredDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requiredDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequiredDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requiredDocument.setId(longCount.incrementAndGet());

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(requiredDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequiredDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequiredDocument() throws Exception {
        // Initialize the database
        insertedRequiredDocument = requiredDocumentRepository.saveAndFlush(requiredDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the requiredDocument
        restRequiredDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, requiredDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return requiredDocumentRepository.count();
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

    protected RequiredDocument getPersistedRequiredDocument(RequiredDocument requiredDocument) {
        return requiredDocumentRepository.findById(requiredDocument.getId()).orElseThrow();
    }

    protected void assertPersistedRequiredDocumentToMatchAllProperties(RequiredDocument expectedRequiredDocument) {
        assertRequiredDocumentAllPropertiesEquals(expectedRequiredDocument, getPersistedRequiredDocument(expectedRequiredDocument));
    }

    protected void assertPersistedRequiredDocumentToMatchUpdatableProperties(RequiredDocument expectedRequiredDocument) {
        assertRequiredDocumentAllUpdatablePropertiesEquals(
            expectedRequiredDocument,
            getPersistedRequiredDocument(expectedRequiredDocument)
        );
    }
}
