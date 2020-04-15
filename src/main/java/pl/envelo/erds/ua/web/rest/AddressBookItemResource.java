package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.AddressBookItemService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;
import pl.envelo.erds.ua.service.dto.AddressBookItemCriteria;
import pl.envelo.erds.ua.service.AddressBookItemQueryService;

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
 * REST controller for managing {@link pl.envelo.erds.ua.domain.AddressBookItem}.
 */
@RestController
@RequestMapping("/api")
public class AddressBookItemResource {

    private final Logger log = LoggerFactory.getLogger(AddressBookItemResource.class);

    private static final String ENTITY_NAME = "useragentAddressBookItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddressBookItemService addressBookItemService;

    private final AddressBookItemQueryService addressBookItemQueryService;

    public AddressBookItemResource(AddressBookItemService addressBookItemService, AddressBookItemQueryService addressBookItemQueryService) {
        this.addressBookItemService = addressBookItemService;
        this.addressBookItemQueryService = addressBookItemQueryService;
    }

    /**
     * {@code POST  /address-book-items} : Create a new addressBookItem.
     *
     * @param addressBookItemDTO the addressBookItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addressBookItemDTO, or with status {@code 400 (Bad Request)} if the addressBookItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/address-book-items")
    public ResponseEntity<AddressBookItemDTO> createAddressBookItem(@RequestBody AddressBookItemDTO addressBookItemDTO) throws URISyntaxException {
        log.debug("REST request to save AddressBookItem : {}", addressBookItemDTO);
        if (addressBookItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new addressBookItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressBookItemDTO result = addressBookItemService.save(addressBookItemDTO);
        return ResponseEntity.created(new URI("/api/address-book-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /address-book-items} : Updates an existing addressBookItem.
     *
     * @param addressBookItemDTO the addressBookItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressBookItemDTO,
     * or with status {@code 400 (Bad Request)} if the addressBookItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addressBookItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/address-book-items")
    public ResponseEntity<AddressBookItemDTO> updateAddressBookItem(@RequestBody AddressBookItemDTO addressBookItemDTO) throws URISyntaxException {
        log.debug("REST request to update AddressBookItem : {}", addressBookItemDTO);
        if (addressBookItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AddressBookItemDTO result = addressBookItemService.save(addressBookItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressBookItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /address-book-items} : get all the addressBookItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addressBookItems in body.
     */
    @GetMapping("/address-book-items")
    public ResponseEntity<List<AddressBookItemDTO>> getAllAddressBookItems(AddressBookItemCriteria criteria) {
        log.debug("REST request to get AddressBookItems by criteria: {}", criteria);
        List<AddressBookItemDTO> entityList = addressBookItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /address-book-items/count} : count all the addressBookItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/address-book-items/count")
    public ResponseEntity<Long> countAddressBookItems(AddressBookItemCriteria criteria) {
        log.debug("REST request to count AddressBookItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(addressBookItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /address-book-items/:id} : get the "id" addressBookItem.
     *
     * @param id the id of the addressBookItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressBookItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/address-book-items/{id}")
    public ResponseEntity<AddressBookItemDTO> getAddressBookItem(@PathVariable Long id) {
        log.debug("REST request to get AddressBookItem : {}", id);
        Optional<AddressBookItemDTO> addressBookItemDTO = addressBookItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addressBookItemDTO);
    }

    /**
     * {@code DELETE  /address-book-items/:id} : delete the "id" addressBookItem.
     *
     * @param id the id of the addressBookItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/address-book-items/{id}")
    public ResponseEntity<Void> deleteAddressBookItem(@PathVariable Long id) {
        log.debug("REST request to delete AddressBookItem : {}", id);
        addressBookItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
