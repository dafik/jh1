package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.EvidenceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Evidence}.
 */
public interface EvidenceService {

    /**
     * Save a evidence.
     *
     * @param evidenceDTO the entity to save.
     * @return the persisted entity.
     */
    EvidenceDTO save(EvidenceDTO evidenceDTO);

    /**
     * Get all the evidences.
     *
     * @return the list of entities.
     */
    List<EvidenceDTO> findAll();

    /**
     * Get the "id" evidence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvidenceDTO> findOne(Long id);

    /**
     * Delete the "id" evidence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
