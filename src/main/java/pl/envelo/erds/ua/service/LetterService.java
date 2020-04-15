package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.LetterDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Letter}.
 */
public interface LetterService {

    /**
     * Save a letter.
     *
     * @param letterDTO the entity to save.
     * @return the persisted entity.
     */
    LetterDTO save(LetterDTO letterDTO);

    /**
     * Get all the letters.
     *
     * @return the list of entities.
     */
    List<LetterDTO> findAll();

    /**
     * Get the "id" letter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LetterDTO> findOne(Long id);

    /**
     * Delete the "id" letter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
