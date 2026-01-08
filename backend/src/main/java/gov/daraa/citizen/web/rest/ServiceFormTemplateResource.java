package gov.daraa.citizen.web.rest;

import gov.daraa.citizen.repository.ServiceFormTemplateRepository;
import gov.daraa.citizen.service.ServiceFormTemplateService;
import gov.daraa.citizen.service.dto.ServiceFormTemplateDTO;
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
 * REST controller for managing {@link gov.daraa.citizen.domain.ServiceFormTemplate}.
 */
@RestController
@RequestMapping("/api/service-form-templates")
public class ServiceFormTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceFormTemplateResource.class);

    private static final String ENTITY_NAME = "serviceFormTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceFormTemplateService serviceFormTemplateService;

    private final ServiceFormTemplateRepository serviceFormTemplateRepository;

    public ServiceFormTemplateResource(
        ServiceFormTemplateService serviceFormTemplateService,
        ServiceFormTemplateRepository serviceFormTemplateRepository
    ) {
        this.serviceFormTemplateService = serviceFormTemplateService;
        this.serviceFormTemplateRepository = serviceFormTemplateRepository;
    }

    /**
     * {@code POST  /service-form-templates} : Create a new serviceFormTemplate.
     *
     * @param serviceFormTemplateDTO the serviceFormTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceFormTemplateDTO, or with status {@code 400 (Bad Request)} if the serviceFormTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceFormTemplateDTO> createServiceFormTemplate(
        @Valid @RequestBody ServiceFormTemplateDTO serviceFormTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ServiceFormTemplate : {}", serviceFormTemplateDTO);
        if (serviceFormTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceFormTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceFormTemplateDTO = serviceFormTemplateService.save(serviceFormTemplateDTO);
        return ResponseEntity.created(new URI("/api/service-form-templates/" + serviceFormTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceFormTemplateDTO.getId().toString()))
            .body(serviceFormTemplateDTO);
    }

    /**
     * {@code PUT  /service-form-templates/:id} : Updates an existing serviceFormTemplate.
     *
     * @param id the id of the serviceFormTemplateDTO to save.
     * @param serviceFormTemplateDTO the serviceFormTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceFormTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the serviceFormTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceFormTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceFormTemplateDTO> updateServiceFormTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceFormTemplateDTO serviceFormTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceFormTemplate : {}, {}", id, serviceFormTemplateDTO);
        if (serviceFormTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceFormTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceFormTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceFormTemplateDTO = serviceFormTemplateService.update(serviceFormTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceFormTemplateDTO.getId().toString()))
            .body(serviceFormTemplateDTO);
    }

    /**
     * {@code PATCH  /service-form-templates/:id} : Partial updates given fields of an existing serviceFormTemplate, field will ignore if it is null
     *
     * @param id the id of the serviceFormTemplateDTO to save.
     * @param serviceFormTemplateDTO the serviceFormTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceFormTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the serviceFormTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceFormTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceFormTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceFormTemplateDTO> partialUpdateServiceFormTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceFormTemplateDTO serviceFormTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceFormTemplate partially : {}, {}", id, serviceFormTemplateDTO);
        if (serviceFormTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceFormTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceFormTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceFormTemplateDTO> result = serviceFormTemplateService.partialUpdate(serviceFormTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceFormTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-form-templates} : get all the serviceFormTemplates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceFormTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceFormTemplateDTO>> getAllServiceFormTemplates(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ServiceFormTemplates");
        Page<ServiceFormTemplateDTO> page = serviceFormTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-form-templates/:id} : get the "id" serviceFormTemplate.
     *
     * @param id the id of the serviceFormTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceFormTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceFormTemplateDTO> getServiceFormTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceFormTemplate : {}", id);
        Optional<ServiceFormTemplateDTO> serviceFormTemplateDTO = serviceFormTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceFormTemplateDTO);
    }

    /**
     * {@code DELETE  /service-form-templates/:id} : delete the "id" serviceFormTemplate.
     *
     * @param id the id of the serviceFormTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceFormTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceFormTemplate : {}", id);
        serviceFormTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
