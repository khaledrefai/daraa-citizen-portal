package gov.daraa.citizen.web.rest;

import gov.daraa.citizen.repository.CitizenServiceRepository;
import gov.daraa.citizen.service.CitizenServiceService;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
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
 * REST controller for managing {@link gov.daraa.citizen.domain.CitizenService}.
 */
@RestController
@RequestMapping("/api/citizen-services")
public class CitizenServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(CitizenServiceResource.class);

    private static final String ENTITY_NAME = "citizenService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitizenServiceService citizenServiceService;

    private final CitizenServiceRepository citizenServiceRepository;

    public CitizenServiceResource(CitizenServiceService citizenServiceService, CitizenServiceRepository citizenServiceRepository) {
        this.citizenServiceService = citizenServiceService;
        this.citizenServiceRepository = citizenServiceRepository;
    }

    /**
     * {@code POST  /citizen-services} : Create a new citizenService.
     *
     * @param citizenServiceDTO the citizenServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citizenServiceDTO, or with status {@code 400 (Bad Request)} if the citizenService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CitizenServiceDTO> createCitizenService(@Valid @RequestBody CitizenServiceDTO citizenServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CitizenService : {}", citizenServiceDTO);
        if (citizenServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new citizenService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        citizenServiceDTO = citizenServiceService.save(citizenServiceDTO);
        return ResponseEntity.created(new URI("/api/citizen-services/" + citizenServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, citizenServiceDTO.getId().toString()))
            .body(citizenServiceDTO);
    }

    /**
     * {@code PUT  /citizen-services/:id} : Updates an existing citizenService.
     *
     * @param id the id of the citizenServiceDTO to save.
     * @param citizenServiceDTO the citizenServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citizenServiceDTO,
     * or with status {@code 400 (Bad Request)} if the citizenServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citizenServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CitizenServiceDTO> updateCitizenService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CitizenServiceDTO citizenServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CitizenService : {}, {}", id, citizenServiceDTO);
        if (citizenServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citizenServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citizenServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        citizenServiceDTO = citizenServiceService.update(citizenServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citizenServiceDTO.getId().toString()))
            .body(citizenServiceDTO);
    }

    /**
     * {@code PATCH  /citizen-services/:id} : Partial updates given fields of an existing citizenService, field will ignore if it is null
     *
     * @param id the id of the citizenServiceDTO to save.
     * @param citizenServiceDTO the citizenServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citizenServiceDTO,
     * or with status {@code 400 (Bad Request)} if the citizenServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the citizenServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the citizenServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitizenServiceDTO> partialUpdateCitizenService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CitizenServiceDTO citizenServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CitizenService partially : {}, {}", id, citizenServiceDTO);
        if (citizenServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citizenServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citizenServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitizenServiceDTO> result = citizenServiceService.partialUpdate(citizenServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citizenServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /citizen-services} : get all the citizenServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citizenServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CitizenServiceDTO>> getAllCitizenServices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CitizenServices");
        Page<CitizenServiceDTO> page = citizenServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /citizen-services/:id} : get the "id" citizenService.
     *
     * @param id the id of the citizenServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citizenServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CitizenServiceDTO> getCitizenService(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CitizenService : {}", id);
        Optional<CitizenServiceDTO> citizenServiceDTO = citizenServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citizenServiceDTO);
    }

    /**
     * {@code DELETE  /citizen-services/:id} : delete the "id" citizenService.
     *
     * @param id the id of the citizenServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCitizenService(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CitizenService : {}", id);
        citizenServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
