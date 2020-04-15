package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.OutgoingMeta}.
 */
public interface OutgoingMetaService {

    /**
     * Save a outgoingMeta.
     *
     * @param outgoingMetaDTO the entity to save.
     * @return the persisted entity.
     */
    OutgoingMetaDTO save(OutgoingMetaDTO outgoingMetaDTO);

    /**
     * Get all the outgoingMetas.
     *
     * @return the list of entities.
     */
    List<OutgoingMetaDTO> findAll();
    /**
     * Get all the OutgoingMetaDTO where LetterGroup is {@code null}.
     *
     * @return the list of entities.
     */
    List<OutgoingMetaDTO> findAllWhereLetterGroupIsNull();

    /**
     * Get the "id" outgoingMeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutgoingMetaDTO> findOne(Long id);

    /**
     * Delete the "id" outgoingMeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
