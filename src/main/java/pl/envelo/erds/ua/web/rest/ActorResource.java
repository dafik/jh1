package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.ActorService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.ActorDTO;
import pl.envelo.erds.ua.service.dto.ActorCriteria;
import pl.envelo.erds.ua.service.ActorQueryService;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.Actor}.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "useragentActor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActorService actorService;

    private final ActorQueryService actorQueryService;

    public ActorResource(ActorService actorService, ActorQueryService actorQueryService) {
        this.actorService = actorService;
        this.actorQueryService = actorQueryService;
    }

    /**
     * {@code POST  /actors} : Create a new actor.
     *
     * @param actorDTO the actorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actorDTO, or with status {@code 400 (Bad Request)} if the actor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actors")
    public ResponseEntity<ActorDTO> createActor(@RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getId() != null) {
            throw new BadRequestAlertException("A new actor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actors} : Updates an existing actor.
     *
     * @param actorDTO the actorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actorDTO,
     * or with status {@code 400 (Bad Request)} if the actorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actors")
    public ResponseEntity<ActorDTO> updateActor(@RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actorDTO);
        if (actorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /actors} : get all the actors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actors in body.
     */
    @GetMapping("/actors")
    public ResponseEntity<List<ActorDTO>> getAllActors(ActorCriteria criteria) {
        log.debug("REST request to get Actors by criteria: {}", criteria);
        List<ActorDTO> entityList = actorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /actors/count} : count all the actors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/actors/count")
    public ResponseEntity<Long> countActors(ActorCriteria criteria) {
        log.debug("REST request to count Actors by criteria: {}", criteria);
        return ResponseEntity.ok().body(actorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /actors/:id} : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actors/{id}")
    public ResponseEntity<ActorDTO> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        Optional<ActorDTO> actorDTO = actorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actorDTO);
    }

    /**
     * {@code DELETE  /actors/:id} : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actors/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
