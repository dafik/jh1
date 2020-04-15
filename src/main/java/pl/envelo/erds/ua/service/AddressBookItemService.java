package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.AddressBookItem}.
 */
public interface AddressBookItemService {

    /**
     * Save a addressBookItem.
     *
     * @param addressBookItemDTO the entity to save.
     * @return the persisted entity.
     */
    AddressBookItemDTO save(AddressBookItemDTO addressBookItemDTO);

    /**
     * Get all the addressBookItems.
     *
     * @return the list of entities.
     */
    List<AddressBookItemDTO> findAll();

    /**
     * Get the "id" addressBookItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddressBookItemDTO> findOne(Long id);

    /**
     * Delete the "id" addressBookItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
