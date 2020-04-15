package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.ActorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Actor}.
 */
public interface ActorService {

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save.
     * @return the persisted entity.
     */
    ActorDTO save(ActorDTO actorDTO);

    /**
     * Get all the actors.
     *
     * @return the list of entities.
     */
    List<ActorDTO> findAll();

    /**
     * Get the "id" actor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActorDTO> findOne(Long id);

    /**
     * Delete the "id" actor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
