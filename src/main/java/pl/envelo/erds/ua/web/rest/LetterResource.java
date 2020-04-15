package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.LetterService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.LetterDTO;
import pl.envelo.erds.ua.service.dto.LetterCriteria;
import pl.envelo.erds.ua.service.LetterQueryService;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.Letter}.
 */
@RestController
@RequestMapping("/api")
public class LetterResource {

    private final Logger log = LoggerFactory.getLogger(LetterResource.class);

    private static final String ENTITY_NAME = "useragentLetter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LetterService letterService;

    private final LetterQueryService letterQueryService;

    public LetterResource(LetterService letterService, LetterQueryService letterQueryService) {
        this.letterService = letterService;
        this.letterQueryService = letterQueryService;
    }

    /**
     * {@code POST  /letters} : Create a new letter.
     *
     * @param letterDTO the letterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new letterDTO, or with status {@code 400 (Bad Request)} if the letter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/letters")
    public ResponseEntity<LetterDTO> createLetter(@RequestBody LetterDTO letterDTO) throws URISyntaxException {
        log.debug("REST request to save Letter : {}", letterDTO);
        if (letterDTO.getId() != null) {
            throw new BadRequestAlertException("A new letter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LetterDTO result = letterService.save(letterDTO);
        return ResponseEntity.created(new URI("/api/letters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /letters} : Updates an existing letter.
     *
     * @param letterDTO the letterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated letterDTO,
     * or with status {@code 400 (Bad Request)} if the letterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the letterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/letters")
    public ResponseEntity<LetterDTO> updateLetter(@RequestBody LetterDTO letterDTO) throws URISyntaxException {
        log.debug("REST request to update Letter : {}", letterDTO);
        if (letterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LetterDTO result = letterService.save(letterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, letterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /letters} : get all the letters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of letters in body.
     */
    @GetMapping("/letters")
    public ResponseEntity<List<LetterDTO>> getAllLetters(LetterCriteria criteria) {
        log.debug("REST request to get Letters by criteria: {}", criteria);
        List<LetterDTO> entityList = letterQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /letters/count} : count all the letters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/letters/count")
    public ResponseEntity<Long> countLetters(LetterCriteria criteria) {
        log.debug("REST request to count Letters by criteria: {}", criteria);
        return ResponseEntity.ok().body(letterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /letters/:id} : get the "id" letter.
     *
     * @param id the id of the letterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the letterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/letters/{id}")
    public ResponseEntity<LetterDTO> getLetter(@PathVariable Long id) {
        log.debug("REST request to get Letter : {}", id);
        Optional<LetterDTO> letterDTO = letterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(letterDTO);
    }

    /**
     * {@code DELETE  /letters/:id} : delete the "id" letter.
     *
     * @param id the id of the letterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/letters/{id}")
    public ResponseEntity<Void> deleteLetter(@PathVariable Long id) {
        log.debug("REST request to delete Letter : {}", id);
        letterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
