package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.ThreadDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Thread}.
 */
public interface ThreadService {

    /**
     * Save a thread.
     *
     * @param threadDTO the entity to save.
     * @return the persisted entity.
     */
    ThreadDTO save(ThreadDTO threadDTO);

    /**
     * Get all the threads.
     *
     * @return the list of entities.
     */
    List<ThreadDTO> findAll();

    /**
     * Get the "id" thread.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ThreadDTO> findOne(Long id);

    /**
     * Delete the "id" thread.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
