package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.EvidenceService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.EvidenceDTO;
import pl.envelo.erds.ua.service.dto.EvidenceCriteria;
import pl.envelo.erds.ua.service.EvidenceQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pl.envelo.erds.ua.domain.Evidence}.
 */
@RestController
@RequestMapping("/api")
public class EvidenceResource {

    private final Logger log = LoggerFactory.getLogger(EvidenceResource.class);

    private static final String ENTITY_NAME = "useragentEvidence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvidenceService evidenceService;

    private final EvidenceQueryService evidenceQueryService;

    public EvidenceResource(EvidenceService evidenceService, EvidenceQueryService evidenceQueryService) {
        this.evidenceService = evidenceService;
        this.evidenceQueryService = evidenceQueryService;
    }

    /**
     * {@code POST  /evidences} : Create a new evidence.
     *
     * @param evidenceDTO the evidenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evidenceDTO, or with status {@code 400 (Bad Request)} if the evidence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evidences")
    public ResponseEntity<EvidenceDTO> createEvidence(@RequestBody EvidenceDTO evidenceDTO) throws URISyntaxException {
        log.debug("REST request to save Evidence : {}", evidenceDTO);
        if (evidenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new evidence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvidenceDTO result = evidenceService.save(evidenceDTO);
        return ResponseEntity.created(new URI("/api/evidences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evidences} : Updates an existing evidence.
     *
     * @param evidenceDTO the evidenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evidenceDTO,
     * or with status {@code 400 (Bad Request)} if the evidenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evidenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evidences")
    public ResponseEntity<EvidenceDTO> updateEvidence(@RequestBody EvidenceDTO evidenceDTO) throws URISyntaxException {
        log.debug("REST request to update Evidence : {}", evidenceDTO);
        if (evidenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EvidenceDTO result = evidenceService.save(evidenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evidenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /evidences} : get all the evidences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evidences in body.
     */
    @GetMapping("/evidences")
    public ResponseEntity<List<EvidenceDTO>> getAllEvidences(EvidenceCriteria criteria) {
        log.debug("REST request to get Evidences by criteria: {}", criteria);
        List<EvidenceDTO> entityList = evidenceQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /evidences/count} : count all the evidences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/evidences/count")
    public ResponseEntity<Long> countEvidences(EvidenceCriteria criteria) {
        log.debug("REST request to count Evidences by criteria: {}", criteria);
        return ResponseEntity.ok().body(evidenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evidences/:id} : get the "id" evidence.
     *
     * @param id the id of the evidenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evidenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evidences/{id}")
    public ResponseEntity<EvidenceDTO> getEvidence(@PathVariable Long id) {
        log.debug("REST request to get Evidence : {}", id);
        Optional<EvidenceDTO> evidenceDTO = evidenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evidenceDTO);
    }

    /**
     * {@code DELETE  /evidences/:id} : delete the "id" evidence.
     *
     * @param id the id of the evidenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evidences/{id}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long id) {
        log.debug("REST request to delete Evidence : {}", id);
        evidenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
