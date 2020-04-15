package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.BoxDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Box}.
 */
public interface BoxService {

    /**
     * Save a box.
     *
     * @param boxDTO the entity to save.
     * @return the persisted entity.
     */
    BoxDTO save(BoxDTO boxDTO);

    /**
     * Get all the boxes.
     *
     * @return the list of entities.
     */
    List<BoxDTO> findAll();

    /**
     * Get the "id" box.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BoxDTO> findOne(Long id);

    /**
     * Delete the "id" box.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
