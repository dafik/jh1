package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.OutgoingMetaService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;
import pl.envelo.erds.ua.service.dto.OutgoingMetaCriteria;
import pl.envelo.erds.ua.service.OutgoingMetaQueryService;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link pl.envelo.erds.ua.domain.OutgoingMeta}.
 */
@RestController
@RequestMapping("/api")
public class OutgoingMetaResource {

    private final Logger log = LoggerFactory.getLogger(OutgoingMetaResource.class);

    private static final String ENTITY_NAME = "useragentOutgoingMeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutgoingMetaService outgoingMetaService;

    private final OutgoingMetaQueryService outgoingMetaQueryService;

    public OutgoingMetaResource(OutgoingMetaService outgoingMetaService, OutgoingMetaQueryService outgoingMetaQueryService) {
        this.outgoingMetaService = outgoingMetaService;
        this.outgoingMetaQueryService = outgoingMetaQueryService;
    }

    /**
     * {@code POST  /outgoing-metas} : Create a new outgoingMeta.
     *
     * @param outgoingMetaDTO the outgoingMetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outgoingMetaDTO, or with status {@code 400 (Bad Request)} if the outgoingMeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/outgoing-metas")
    public ResponseEntity<OutgoingMetaDTO> createOutgoingMeta(@RequestBody OutgoingMetaDTO outgoingMetaDTO) throws URISyntaxException {
        log.debug("REST request to save OutgoingMeta : {}", outgoingMetaDTO);
        if (outgoingMetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new outgoingMeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutgoingMetaDTO result = outgoingMetaService.save(outgoingMetaDTO);
        return ResponseEntity.created(new URI("/api/outgoing-metas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /outgoing-metas} : Updates an existing outgoingMeta.
     *
     * @param outgoingMetaDTO the outgoingMetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outgoingMetaDTO,
     * or with status {@code 400 (Bad Request)} if the outgoingMetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outgoingMetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/outgoing-metas")
    public ResponseEntity<OutgoingMetaDTO> updateOutgoingMeta(@RequestBody OutgoingMetaDTO outgoingMetaDTO) throws URISyntaxException {
        log.debug("REST request to update OutgoingMeta : {}", outgoingMetaDTO);
        if (outgoingMetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OutgoingMetaDTO result = outgoingMetaService.save(outgoingMetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, outgoingMetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /outgoing-metas} : get all the outgoingMetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outgoingMetas in body.
     */
    @GetMapping("/outgoing-metas")
    public ResponseEntity<List<OutgoingMetaDTO>> getAllOutgoingMetas(OutgoingMetaCriteria criteria) {
        log.debug("REST request to get OutgoingMetas by criteria: {}", criteria);
        List<OutgoingMetaDTO> entityList = outgoingMetaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /outgoing-metas/count} : count all the outgoingMetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/outgoing-metas/count")
    public ResponseEntity<Long> countOutgoingMetas(OutgoingMetaCriteria criteria) {
        log.debug("REST request to count OutgoingMetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(outgoingMetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /outgoing-metas/:id} : get the "id" outgoingMeta.
     *
     * @param id the id of the outgoingMetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outgoingMetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/outgoing-metas/{id}")
    public ResponseEntity<OutgoingMetaDTO> getOutgoingMeta(@PathVariable Long id) {
        log.debug("REST request to get OutgoingMeta : {}", id);
        Optional<OutgoingMetaDTO> outgoingMetaDTO = outgoingMetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outgoingMetaDTO);
    }

    /**
     * {@code DELETE  /outgoing-metas/:id} : delete the "id" outgoingMeta.
     *
     * @param id the id of the outgoingMetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/outgoing-metas/{id}")
    public ResponseEntity<Void> deleteOutgoingMeta(@PathVariable Long id) {
        log.debug("REST request to delete OutgoingMeta : {}", id);
        outgoingMetaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
