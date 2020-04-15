package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.service.NotificationService;
import pl.envelo.erds.ua.web.rest.errors.BadRequestAlertException;
import pl.envelo.erds.ua.service.dto.NotificationDTO;
import pl.envelo.erds.ua.service.dto.NotificationCriteria;
import pl.envelo.erds.ua.service.NotificationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pl.envelo.erds.ua.domain.Notification}.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "useragentNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationService notificationService;

    private final NotificationQueryService notificationQueryService;

    public NotificationResource(NotificationService notificationService, NotificationQueryService notificationQueryService) {
        this.notificationService = notificationService;
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationDTO, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notifications")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notifications} : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notifications")
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(NotificationCriteria criteria) {
        log.debug("REST request to get Notifications by criteria: {}", criteria);
        List<NotificationDTO> entityList = notificationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /notifications/count} : count all the notifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notifications/count")
    public ResponseEntity<Long> countNotifications(NotificationCriteria criteria) {
        log.debug("REST request to count Notifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notifications/{id}")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        Optional<NotificationDTO> notificationDTO = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationDTO);
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
