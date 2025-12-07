package gov.daraa.citizen.web.rest;

import gov.daraa.citizen.repository.RequiredDocumentRepository;
import gov.daraa.citizen.service.RequiredDocumentService;
import gov.daraa.citizen.service.dto.RequiredDocumentDTO;
import gov.daraa.citizen.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link gov.daraa.citizen.domain.RequiredDocument}.
 */
@RestController
@RequestMapping("/api/required-documents")
public class RequiredDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(RequiredDocumentResource.class);

    private static final String ENTITY_NAME = "requiredDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequiredDocumentService requiredDocumentService;

    private final RequiredDocumentRepository requiredDocumentRepository;

    public RequiredDocumentResource(
        RequiredDocumentService requiredDocumentService,
        RequiredDocumentRepository requiredDocumentRepository
    ) {
        this.requiredDocumentService = requiredDocumentService;
        this.requiredDocumentRepository = requiredDocumentRepository;
    }

    /**
     * {@code POST  /required-documents} : Create a new requiredDocument.
     *
     * @param requiredDocumentDTO the requiredDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requiredDocumentDTO, or with status {@code 400 (Bad Request)} if the requiredDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RequiredDocumentDTO> createRequiredDocument(@Valid @RequestBody RequiredDocumentDTO requiredDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RequiredDocument : {}", requiredDocumentDTO);
        if (requiredDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new requiredDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requiredDocumentDTO = requiredDocumentService.save(requiredDocumentDTO);
        return ResponseEntity.created(new URI("/api/required-documents/" + requiredDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, requiredDocumentDTO.getId().toString()))
            .body(requiredDocumentDTO);
    }

    /**
     * {@code PUT  /required-documents/:id} : Updates an existing requiredDocument.
     *
     * @param id the id of the requiredDocumentDTO to save.
     * @param requiredDocumentDTO the requiredDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requiredDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the requiredDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requiredDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequiredDocumentDTO> updateRequiredDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RequiredDocumentDTO requiredDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RequiredDocument : {}, {}", id, requiredDocumentDTO);
        if (requiredDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requiredDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requiredDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        requiredDocumentDTO = requiredDocumentService.update(requiredDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requiredDocumentDTO.getId().toString()))
            .body(requiredDocumentDTO);
    }

    /**
     * {@code PATCH  /required-documents/:id} : Partial updates given fields of an existing requiredDocument, field will ignore if it is null
     *
     * @param id the id of the requiredDocumentDTO to save.
     * @param requiredDocumentDTO the requiredDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requiredDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the requiredDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requiredDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requiredDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RequiredDocumentDTO> partialUpdateRequiredDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RequiredDocumentDTO requiredDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RequiredDocument partially : {}, {}", id, requiredDocumentDTO);
        if (requiredDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requiredDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requiredDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequiredDocumentDTO> result = requiredDocumentService.partialUpdate(requiredDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requiredDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /required-documents} : get all the requiredDocuments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requiredDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RequiredDocumentDTO>> getAllRequiredDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RequiredDocuments");
        Page<RequiredDocumentDTO> page = requiredDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /required-documents/:id} : get the "id" requiredDocument.
     *
     * @param id the id of the requiredDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requiredDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequiredDocumentDTO> getRequiredDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RequiredDocument : {}", id);
        Optional<RequiredDocumentDTO> requiredDocumentDTO = requiredDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requiredDocumentDTO);
    }

    /**
     * {@code DELETE  /required-documents/:id} : delete the "id" requiredDocument.
     *
     * @param id the id of the requiredDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequiredDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RequiredDocument : {}", id);
        requiredDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
