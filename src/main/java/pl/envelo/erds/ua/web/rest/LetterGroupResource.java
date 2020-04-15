package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.LetterGroupService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.LetterGroupDTO;
import pl.envelo.erds.ua.service.dto.LetterGroupCriteria;
import pl.envelo.erds.ua.service.LetterGroupQueryService;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.LetterGroup}.
 */
@RestController
@RequestMapping("/api")
public class LetterGroupResource {

    private final Logger log = LoggerFactory.getLogger(LetterGroupResource.class);

    private static final String ENTITY_NAME = "useragentLetterGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LetterGroupService letterGroupService;

    private final LetterGroupQueryService letterGroupQueryService;

    public LetterGroupResource(LetterGroupService letterGroupService, LetterGroupQueryService letterGroupQueryService) {
        this.letterGroupService = letterGroupService;
        this.letterGroupQueryService = letterGroupQueryService;
    }

    /**
     * {@code POST  /letter-groups} : Create a new letterGroup.
     *
     * @param letterGroupDTO the letterGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new letterGroupDTO, or with status {@code 400 (Bad Request)} if the letterGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/letter-groups")
    public ResponseEntity<LetterGroupDTO> createLetterGroup(@RequestBody LetterGroupDTO letterGroupDTO) throws URISyntaxException {
        log.debug("REST request to save LetterGroup : {}", letterGroupDTO);
        if (letterGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new letterGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LetterGroupDTO result = letterGroupService.save(letterGroupDTO);
        return ResponseEntity.created(new URI("/api/letter-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /letter-groups} : Updates an existing letterGroup.
     *
     * @param letterGroupDTO the letterGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated letterGroupDTO,
     * or with status {@code 400 (Bad Request)} if the letterGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the letterGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/letter-groups")
    public ResponseEntity<LetterGroupDTO> updateLetterGroup(@RequestBody LetterGroupDTO letterGroupDTO) throws URISyntaxException {
        log.debug("REST request to update LetterGroup : {}", letterGroupDTO);
        if (letterGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LetterGroupDTO result = letterGroupService.save(letterGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, letterGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /letter-groups} : get all the letterGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of letterGroups in body.
     */
    @GetMapping("/letter-groups")
    public ResponseEntity<List<LetterGroupDTO>> getAllLetterGroups(LetterGroupCriteria criteria) {
        log.debug("REST request to get LetterGroups by criteria: {}", criteria);
        List<LetterGroupDTO> entityList = letterGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /letter-groups/count} : count all the letterGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/letter-groups/count")
    public ResponseEntity<Long> countLetterGroups(LetterGroupCriteria criteria) {
        log.debug("REST request to count LetterGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(letterGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /letter-groups/:id} : get the "id" letterGroup.
     *
     * @param id the id of the letterGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the letterGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/letter-groups/{id}")
    public ResponseEntity<LetterGroupDTO> getLetterGroup(@PathVariable Long id) {
        log.debug("REST request to get LetterGroup : {}", id);
        Optional<LetterGroupDTO> letterGroupDTO = letterGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(letterGroupDTO);
    }

    /**
     * {@code DELETE  /letter-groups/:id} : delete the "id" letterGroup.
     *
     * @param id the id of the letterGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/letter-groups/{id}")
    public ResponseEntity<Void> deleteLetterGroup(@PathVariable Long id) {
        log.debug("REST request to delete LetterGroup : {}", id);
        letterGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
