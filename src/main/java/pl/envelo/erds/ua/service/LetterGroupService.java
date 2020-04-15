package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.LetterGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.LetterGroup}.
 */
public interface LetterGroupService {

    /**
     * Save a letterGroup.
     *
     * @param letterGroupDTO the entity to save.
     * @return the persisted entity.
     */
    LetterGroupDTO save(LetterGroupDTO letterGroupDTO);

    /**
     * Get all the letterGroups.
     *
     * @return the list of entities.
     */
    List<LetterGroupDTO> findAll();

    /**
     * Get the "id" letterGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LetterGroupDTO> findOne(Long id);

    /**
     * Delete the "id" letterGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
