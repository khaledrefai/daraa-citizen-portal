package gov.daraa.citizen.web.rest;

import gov.daraa.citizen.repository.DirectorateRepository;
import gov.daraa.citizen.service.DirectorateService;
import gov.daraa.citizen.service.dto.DirectorateDTO;
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
 * REST controller for managing {@link gov.daraa.citizen.domain.Directorate}.
 */
@RestController
@RequestMapping("/api/directorates")
public class DirectorateResource {

    private static final Logger LOG = LoggerFactory.getLogger(DirectorateResource.class);

    private static final String ENTITY_NAME = "directorate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirectorateService directorateService;

    private final DirectorateRepository directorateRepository;

    public DirectorateResource(DirectorateService directorateService, DirectorateRepository directorateRepository) {
        this.directorateService = directorateService;
        this.directorateRepository = directorateRepository;
    }

    /**
     * {@code POST  /directorates} : Create a new directorate.
     *
     * @param directorateDTO the directorateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directorateDTO, or with status {@code 400 (Bad Request)} if the directorate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DirectorateDTO> createDirectorate(@Valid @RequestBody DirectorateDTO directorateDTO) throws URISyntaxException {
        LOG.debug("REST request to save Directorate : {}", directorateDTO);
        if (directorateDTO.getId() != null) {
            throw new BadRequestAlertException("A new directorate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        directorateDTO = directorateService.save(directorateDTO);
        return ResponseEntity.created(new URI("/api/directorates/" + directorateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, directorateDTO.getId().toString()))
            .body(directorateDTO);
    }

    /**
     * {@code PUT  /directorates/:id} : Updates an existing directorate.
     *
     * @param id the id of the directorateDTO to save.
     * @param directorateDTO the directorateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directorateDTO,
     * or with status {@code 400 (Bad Request)} if the directorateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directorateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DirectorateDTO> updateDirectorate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DirectorateDTO directorateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Directorate : {}, {}", id, directorateDTO);
        if (directorateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directorateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directorateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        directorateDTO = directorateService.update(directorateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directorateDTO.getId().toString()))
            .body(directorateDTO);
    }

    /**
     * {@code PATCH  /directorates/:id} : Partial updates given fields of an existing directorate, field will ignore if it is null
     *
     * @param id the id of the directorateDTO to save.
     * @param directorateDTO the directorateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directorateDTO,
     * or with status {@code 400 (Bad Request)} if the directorateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the directorateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the directorateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DirectorateDTO> partialUpdateDirectorate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DirectorateDTO directorateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Directorate partially : {}, {}", id, directorateDTO);
        if (directorateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directorateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directorateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DirectorateDTO> result = directorateService.partialUpdate(directorateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directorateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /directorates} : get all the directorates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directorates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DirectorateDTO>> getAllDirectorates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Directorates");
        Page<DirectorateDTO> page = directorateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /directorates/:id} : get the "id" directorate.
     *
     * @param id the id of the directorateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directorateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorateDTO> getDirectorate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Directorate : {}", id);
        Optional<DirectorateDTO> directorateDTO = directorateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(directorateDTO);
    }

    /**
     * {@code DELETE  /directorates/:id} : delete the "id" directorate.
     *
     * @param id the id of the directorateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectorate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Directorate : {}", id);
        directorateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
