package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.UserAccountService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.UserAccountDTO;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.UserAccount}.
 */
@RestController
@RequestMapping("/api")
public class UserAccountResource {

    private final Logger log = LoggerFactory.getLogger(UserAccountResource.class);

    private static final String ENTITY_NAME = "useragentUserAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccountService userAccountService;

    public UserAccountResource(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * {@code POST  /user-accounts} : Create a new userAccount.
     *
     * @param userAccountDTO the userAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccountDTO, or with status {@code 400 (Bad Request)} if the userAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-accounts")
    public ResponseEntity<UserAccountDTO> createUserAccount(@RequestBody UserAccountDTO userAccountDTO) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccountDTO);
        if (userAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAccountDTO result = userAccountService.save(userAccountDTO);
        return ResponseEntity.created(new URI("/api/user-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-accounts} : Updates an existing userAccount.
     *
     * @param userAccountDTO the userAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccountDTO,
     * or with status {@code 400 (Bad Request)} if the userAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-accounts")
    public ResponseEntity<UserAccountDTO> updateUserAccount(@RequestBody UserAccountDTO userAccountDTO) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}", userAccountDTO);
        if (userAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserAccountDTO result = userAccountService.save(userAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-accounts} : get all the userAccounts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccounts in body.
     */
    @GetMapping("/user-accounts")
    public List<UserAccountDTO> getAllUserAccounts(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserAccounts");
        return userAccountService.findAll();
    }

    /**
     * {@code GET  /user-accounts/:id} : get the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-accounts/{id}")
    public ResponseEntity<UserAccountDTO> getUserAccount(@PathVariable Long id) {
        log.debug("REST request to get UserAccount : {}", id);
        Optional<UserAccountDTO> userAccountDTO = userAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAccountDTO);
    }

    /**
     * {@code DELETE  /user-accounts/:id} : delete the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-accounts/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable Long id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
