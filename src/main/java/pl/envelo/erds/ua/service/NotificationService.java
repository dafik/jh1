package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.NotificationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.Notification}.
 */
public interface NotificationService {

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationDTO save(NotificationDTO notificationDTO);

    /**
     * Get all the notifications.
     *
     * @return the list of entities.
     */
    List<NotificationDTO> findAll();

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationDTO> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
