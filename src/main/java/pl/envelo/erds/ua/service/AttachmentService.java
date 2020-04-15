package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.AttachmentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Attachment}.
 */
public interface AttachmentService {

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save.
     * @return the persisted entity.
     */
    AttachmentDTO save(AttachmentDTO attachmentDTO);

    /**
     * Get all the attachments.
     *
     * @return the list of entities.
     */
    List<AttachmentDTO> findAll();

    /**
     * Get the "id" attachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttachmentDTO> findOne(Long id);

    /**
     * Delete the "id" attachment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
