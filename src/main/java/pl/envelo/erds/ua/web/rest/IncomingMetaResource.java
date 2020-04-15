package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.IncomingMetaService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;
import pl.envelo.erds.ua.service.dto.IncomingMetaCriteria;
import pl.envelo.erds.ua.service.IncomingMetaQueryService;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.IncomingMeta}.
 */
@RestController
@RequestMapping("/api")
public class IncomingMetaResource {

    private final Logger log = LoggerFactory.getLogger(IncomingMetaResource.class);

    private static final String ENTITY_NAME = "useragentIncomingMeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomingMetaService incomingMetaService;

    private final IncomingMetaQueryService incomingMetaQueryService;

    public IncomingMetaResource(IncomingMetaService incomingMetaService, IncomingMetaQueryService incomingMetaQueryService) {
        this.incomingMetaService = incomingMetaService;
        this.incomingMetaQueryService = incomingMetaQueryService;
    }

    /**
     * {@code POST  /incoming-metas} : Create a new incomingMeta.
     *
     * @param incomingMetaDTO the incomingMetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomingMetaDTO, or with status {@code 400 (Bad Request)} if the incomingMeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/incoming-metas")
    public ResponseEntity<IncomingMetaDTO> createIncomingMeta(@RequestBody IncomingMetaDTO incomingMetaDTO) throws URISyntaxException {
        log.debug("REST request to save IncomingMeta : {}", incomingMetaDTO);
        if (incomingMetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new incomingMeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IncomingMetaDTO result = incomingMetaService.save(incomingMetaDTO);
        return ResponseEntity.created(new URI("/api/incoming-metas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /incoming-metas} : Updates an existing incomingMeta.
     *
     * @param incomingMetaDTO the incomingMetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomingMetaDTO,
     * or with status {@code 400 (Bad Request)} if the incomingMetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomingMetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/incoming-metas")
    public ResponseEntity<IncomingMetaDTO> updateIncomingMeta(@RequestBody IncomingMetaDTO incomingMetaDTO) throws URISyntaxException {
        log.debug("REST request to update IncomingMeta : {}", incomingMetaDTO);
        if (incomingMetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IncomingMetaDTO result = incomingMetaService.save(incomingMetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingMetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /incoming-metas} : get all the incomingMetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomingMetas in body.
     */
    @GetMapping("/incoming-metas")
    public ResponseEntity<List<IncomingMetaDTO>> getAllIncomingMetas(IncomingMetaCriteria criteria) {
        log.debug("REST request to get IncomingMetas by criteria: {}", criteria);
        List<IncomingMetaDTO> entityList = incomingMetaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /incoming-metas/count} : count all the incomingMetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/incoming-metas/count")
    public ResponseEntity<Long> countIncomingMetas(IncomingMetaCriteria criteria) {
        log.debug("REST request to count IncomingMetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(incomingMetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /incoming-metas/:id} : get the "id" incomingMeta.
     *
     * @param id the id of the incomingMetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomingMetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/incoming-metas/{id}")
    public ResponseEntity<IncomingMetaDTO> getIncomingMeta(@PathVariable Long id) {
        log.debug("REST request to get IncomingMeta : {}", id);
        Optional<IncomingMetaDTO> incomingMetaDTO = incomingMetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomingMetaDTO);
    }

    /**
     * {@code DELETE  /incoming-metas/:id} : delete the "id" incomingMeta.
     *
     * @param id the id of the incomingMetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/incoming-metas/{id}")
    public ResponseEntity<Void> deleteIncomingMeta(@PathVariable Long id) {
        log.debug("REST request to delete IncomingMeta : {}", id);
        incomingMetaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
