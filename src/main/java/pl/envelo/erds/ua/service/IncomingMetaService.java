package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.IncomingMeta}.
 */
public interface IncomingMetaService {

    /**
     * Save a incomingMeta.
     *
     * @param incomingMetaDTO the entity to save.
     * @return the persisted entity.
     */
    IncomingMetaDTO save(IncomingMetaDTO incomingMetaDTO);

    /**
     * Get all the incomingMetas.
     *
     * @return the list of entities.
     */
    List<IncomingMetaDTO> findAll();
    /**
     * Get all the IncomingMetaDTO where LetterGroup is {@code null}.
     *
     * @return the list of entities.
     */
    List<IncomingMetaDTO> findAllWhereLetterGroupIsNull();

    /**
     * Get the "id" incomingMeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IncomingMetaDTO> findOne(Long id);

    /**
     * Delete the "id" incomingMeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
