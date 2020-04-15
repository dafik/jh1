package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Notification;
import pl.envelo.erds.ua.domain.Box;
import pl.envelo.erds.ua.repository.NotificationRepository;
import pl.envelo.erds.ua.service.NotificationService;
import pl.envelo.erds.ua.service.dto.NotificationDTO;
import pl.envelo.erds.ua.service.mapper.NotificationMapper;
import pl.envelo.erds.ua.service.dto.NotificationCriteria;
import pl.envelo.erds.ua.service.NotificationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static pl.envelo.erds.ua.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pl.envelo.erds.ua.domain.enumeration.NotificationState;
/**
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final String DEFAULT_EAN = "AAAAAAAAAA";
    private static final String UPDATED_EAN = "BBBBBBBBBB";

    private static final String DEFAULT_ERDS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ERDS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROCESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final NotificationState DEFAULT_STATE = NotificationState.NEW;
    private static final NotificationState UPDATED_STATE = NotificationState.ACCEPTED;

    private static final String DEFAULT_SENDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_LABEL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SEND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SEND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DECISION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DECISION_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DECISION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_EXPIRE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXPIRE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .ean(DEFAULT_EAN)
            .erdsId(DEFAULT_ERDS_ID)
            .processId(DEFAULT_PROCESS_ID)
            .subject(DEFAULT_SUBJECT)
            .state(DEFAULT_STATE)
            .senderId(DEFAULT_SENDER_ID)
            .senderLabel(DEFAULT_SENDER_LABEL)
            .sendAt(DEFAULT_SEND_AT)
            .decisionAt(DEFAULT_DECISION_AT)
            .expireAt(DEFAULT_EXPIRE_AT);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID)
            .processId(UPDATED_PROCESS_ID)
            .subject(UPDATED_SUBJECT)
            .state(UPDATED_STATE)
            .senderId(UPDATED_SENDER_ID)
            .senderLabel(UPDATED_SENDER_LABEL)
            .sendAt(UPDATED_SEND_AT)
            .decisionAt(UPDATED_DECISION_AT)
            .expireAt(UPDATED_EXPIRE_AT);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        restNotificationMockMvc.perform(post("/api/notifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getEan()).isEqualTo(DEFAULT_EAN);
        assertThat(testNotification.getErdsId()).isEqualTo(DEFAULT_ERDS_ID);
        assertThat(testNotification.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
        assertThat(testNotification.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotification.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testNotification.getSenderId()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testNotification.getSenderLabel()).isEqualTo(DEFAULT_SENDER_LABEL);
        assertThat(testNotification.getSendAt()).isEqualTo(DEFAULT_SEND_AT);
        assertThat(testNotification.getDecisionAt()).isEqualTo(DEFAULT_DECISION_AT);
        assertThat(testNotification.getExpireAt()).isEqualTo(DEFAULT_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID)))
            .andExpect(jsonPath("$.[*].senderLabel").value(hasItem(DEFAULT_SENDER_LABEL)))
            .andExpect(jsonPath("$.[*].sendAt").value(hasItem(sameInstant(DEFAULT_SEND_AT))))
            .andExpect(jsonPath("$.[*].decisionAt").value(hasItem(sameInstant(DEFAULT_DECISION_AT))))
            .andExpect(jsonPath("$.[*].expireAt").value(hasItem(sameInstant(DEFAULT_EXPIRE_AT))));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.ean").value(DEFAULT_EAN))
            .andExpect(jsonPath("$.erdsId").value(DEFAULT_ERDS_ID))
            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.senderId").value(DEFAULT_SENDER_ID))
            .andExpect(jsonPath("$.senderLabel").value(DEFAULT_SENDER_LABEL))
            .andExpect(jsonPath("$.sendAt").value(sameInstant(DEFAULT_SEND_AT)))
            .andExpect(jsonPath("$.decisionAt").value(sameInstant(DEFAULT_DECISION_AT)))
            .andExpect(jsonPath("$.expireAt").value(sameInstant(DEFAULT_EXPIRE_AT)));
    }


    @Test
    @Transactional
    public void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotificationsByEanIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean equals to DEFAULT_EAN
        defaultNotificationShouldBeFound("ean.equals=" + DEFAULT_EAN);

        // Get all the notificationList where ean equals to UPDATED_EAN
        defaultNotificationShouldNotBeFound("ean.equals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllNotificationsByEanIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean not equals to DEFAULT_EAN
        defaultNotificationShouldNotBeFound("ean.notEquals=" + DEFAULT_EAN);

        // Get all the notificationList where ean not equals to UPDATED_EAN
        defaultNotificationShouldBeFound("ean.notEquals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllNotificationsByEanIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean in DEFAULT_EAN or UPDATED_EAN
        defaultNotificationShouldBeFound("ean.in=" + DEFAULT_EAN + "," + UPDATED_EAN);

        // Get all the notificationList where ean equals to UPDATED_EAN
        defaultNotificationShouldNotBeFound("ean.in=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllNotificationsByEanIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean is not null
        defaultNotificationShouldBeFound("ean.specified=true");

        // Get all the notificationList where ean is null
        defaultNotificationShouldNotBeFound("ean.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByEanContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean contains DEFAULT_EAN
        defaultNotificationShouldBeFound("ean.contains=" + DEFAULT_EAN);

        // Get all the notificationList where ean contains UPDATED_EAN
        defaultNotificationShouldNotBeFound("ean.contains=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllNotificationsByEanNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where ean does not contain DEFAULT_EAN
        defaultNotificationShouldNotBeFound("ean.doesNotContain=" + DEFAULT_EAN);

        // Get all the notificationList where ean does not contain UPDATED_EAN
        defaultNotificationShouldBeFound("ean.doesNotContain=" + UPDATED_EAN);
    }


    @Test
    @Transactional
    public void getAllNotificationsByErdsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId equals to DEFAULT_ERDS_ID
        defaultNotificationShouldBeFound("erdsId.equals=" + DEFAULT_ERDS_ID);

        // Get all the notificationList where erdsId equals to UPDATED_ERDS_ID
        defaultNotificationShouldNotBeFound("erdsId.equals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByErdsIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId not equals to DEFAULT_ERDS_ID
        defaultNotificationShouldNotBeFound("erdsId.notEquals=" + DEFAULT_ERDS_ID);

        // Get all the notificationList where erdsId not equals to UPDATED_ERDS_ID
        defaultNotificationShouldBeFound("erdsId.notEquals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByErdsIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId in DEFAULT_ERDS_ID or UPDATED_ERDS_ID
        defaultNotificationShouldBeFound("erdsId.in=" + DEFAULT_ERDS_ID + "," + UPDATED_ERDS_ID);

        // Get all the notificationList where erdsId equals to UPDATED_ERDS_ID
        defaultNotificationShouldNotBeFound("erdsId.in=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByErdsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId is not null
        defaultNotificationShouldBeFound("erdsId.specified=true");

        // Get all the notificationList where erdsId is null
        defaultNotificationShouldNotBeFound("erdsId.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByErdsIdContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId contains DEFAULT_ERDS_ID
        defaultNotificationShouldBeFound("erdsId.contains=" + DEFAULT_ERDS_ID);

        // Get all the notificationList where erdsId contains UPDATED_ERDS_ID
        defaultNotificationShouldNotBeFound("erdsId.contains=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByErdsIdNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where erdsId does not contain DEFAULT_ERDS_ID
        defaultNotificationShouldNotBeFound("erdsId.doesNotContain=" + DEFAULT_ERDS_ID);

        // Get all the notificationList where erdsId does not contain UPDATED_ERDS_ID
        defaultNotificationShouldBeFound("erdsId.doesNotContain=" + UPDATED_ERDS_ID);
    }


    @Test
    @Transactional
    public void getAllNotificationsByProcessIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId equals to DEFAULT_PROCESS_ID
        defaultNotificationShouldBeFound("processId.equals=" + DEFAULT_PROCESS_ID);

        // Get all the notificationList where processId equals to UPDATED_PROCESS_ID
        defaultNotificationShouldNotBeFound("processId.equals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByProcessIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId not equals to DEFAULT_PROCESS_ID
        defaultNotificationShouldNotBeFound("processId.notEquals=" + DEFAULT_PROCESS_ID);

        // Get all the notificationList where processId not equals to UPDATED_PROCESS_ID
        defaultNotificationShouldBeFound("processId.notEquals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByProcessIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId in DEFAULT_PROCESS_ID or UPDATED_PROCESS_ID
        defaultNotificationShouldBeFound("processId.in=" + DEFAULT_PROCESS_ID + "," + UPDATED_PROCESS_ID);

        // Get all the notificationList where processId equals to UPDATED_PROCESS_ID
        defaultNotificationShouldNotBeFound("processId.in=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByProcessIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId is not null
        defaultNotificationShouldBeFound("processId.specified=true");

        // Get all the notificationList where processId is null
        defaultNotificationShouldNotBeFound("processId.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByProcessIdContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId contains DEFAULT_PROCESS_ID
        defaultNotificationShouldBeFound("processId.contains=" + DEFAULT_PROCESS_ID);

        // Get all the notificationList where processId contains UPDATED_PROCESS_ID
        defaultNotificationShouldNotBeFound("processId.contains=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsByProcessIdNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where processId does not contain DEFAULT_PROCESS_ID
        defaultNotificationShouldNotBeFound("processId.doesNotContain=" + DEFAULT_PROCESS_ID);

        // Get all the notificationList where processId does not contain UPDATED_PROCESS_ID
        defaultNotificationShouldBeFound("processId.doesNotContain=" + UPDATED_PROCESS_ID);
    }


    @Test
    @Transactional
    public void getAllNotificationsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject equals to DEFAULT_SUBJECT
        defaultNotificationShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject equals to UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject not equals to DEFAULT_SUBJECT
        defaultNotificationShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject not equals to UPDATED_SUBJECT
        defaultNotificationShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultNotificationShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the notificationList where subject equals to UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject is not null
        defaultNotificationShouldBeFound("subject.specified=true");

        // Get all the notificationList where subject is null
        defaultNotificationShouldNotBeFound("subject.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject contains DEFAULT_SUBJECT
        defaultNotificationShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject contains UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where subject does not contain DEFAULT_SUBJECT
        defaultNotificationShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject does not contain UPDATED_SUBJECT
        defaultNotificationShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }


    @Test
    @Transactional
    public void getAllNotificationsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where state equals to DEFAULT_STATE
        defaultNotificationShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the notificationList where state equals to UPDATED_STATE
        defaultNotificationShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where state not equals to DEFAULT_STATE
        defaultNotificationShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the notificationList where state not equals to UPDATED_STATE
        defaultNotificationShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where state in DEFAULT_STATE or UPDATED_STATE
        defaultNotificationShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the notificationList where state equals to UPDATED_STATE
        defaultNotificationShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where state is not null
        defaultNotificationShouldBeFound("state.specified=true");

        // Get all the notificationList where state is null
        defaultNotificationShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId equals to DEFAULT_SENDER_ID
        defaultNotificationShouldBeFound("senderId.equals=" + DEFAULT_SENDER_ID);

        // Get all the notificationList where senderId equals to UPDATED_SENDER_ID
        defaultNotificationShouldNotBeFound("senderId.equals=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId not equals to DEFAULT_SENDER_ID
        defaultNotificationShouldNotBeFound("senderId.notEquals=" + DEFAULT_SENDER_ID);

        // Get all the notificationList where senderId not equals to UPDATED_SENDER_ID
        defaultNotificationShouldBeFound("senderId.notEquals=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId in DEFAULT_SENDER_ID or UPDATED_SENDER_ID
        defaultNotificationShouldBeFound("senderId.in=" + DEFAULT_SENDER_ID + "," + UPDATED_SENDER_ID);

        // Get all the notificationList where senderId equals to UPDATED_SENDER_ID
        defaultNotificationShouldNotBeFound("senderId.in=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId is not null
        defaultNotificationShouldBeFound("senderId.specified=true");

        // Get all the notificationList where senderId is null
        defaultNotificationShouldNotBeFound("senderId.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsBySenderIdContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId contains DEFAULT_SENDER_ID
        defaultNotificationShouldBeFound("senderId.contains=" + DEFAULT_SENDER_ID);

        // Get all the notificationList where senderId contains UPDATED_SENDER_ID
        defaultNotificationShouldNotBeFound("senderId.contains=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderIdNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderId does not contain DEFAULT_SENDER_ID
        defaultNotificationShouldNotBeFound("senderId.doesNotContain=" + DEFAULT_SENDER_ID);

        // Get all the notificationList where senderId does not contain UPDATED_SENDER_ID
        defaultNotificationShouldBeFound("senderId.doesNotContain=" + UPDATED_SENDER_ID);
    }


    @Test
    @Transactional
    public void getAllNotificationsBySenderLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel equals to DEFAULT_SENDER_LABEL
        defaultNotificationShouldBeFound("senderLabel.equals=" + DEFAULT_SENDER_LABEL);

        // Get all the notificationList where senderLabel equals to UPDATED_SENDER_LABEL
        defaultNotificationShouldNotBeFound("senderLabel.equals=" + UPDATED_SENDER_LABEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel not equals to DEFAULT_SENDER_LABEL
        defaultNotificationShouldNotBeFound("senderLabel.notEquals=" + DEFAULT_SENDER_LABEL);

        // Get all the notificationList where senderLabel not equals to UPDATED_SENDER_LABEL
        defaultNotificationShouldBeFound("senderLabel.notEquals=" + UPDATED_SENDER_LABEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderLabelIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel in DEFAULT_SENDER_LABEL or UPDATED_SENDER_LABEL
        defaultNotificationShouldBeFound("senderLabel.in=" + DEFAULT_SENDER_LABEL + "," + UPDATED_SENDER_LABEL);

        // Get all the notificationList where senderLabel equals to UPDATED_SENDER_LABEL
        defaultNotificationShouldNotBeFound("senderLabel.in=" + UPDATED_SENDER_LABEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel is not null
        defaultNotificationShouldBeFound("senderLabel.specified=true");

        // Get all the notificationList where senderLabel is null
        defaultNotificationShouldNotBeFound("senderLabel.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsBySenderLabelContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel contains DEFAULT_SENDER_LABEL
        defaultNotificationShouldBeFound("senderLabel.contains=" + DEFAULT_SENDER_LABEL);

        // Get all the notificationList where senderLabel contains UPDATED_SENDER_LABEL
        defaultNotificationShouldNotBeFound("senderLabel.contains=" + UPDATED_SENDER_LABEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySenderLabelNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where senderLabel does not contain DEFAULT_SENDER_LABEL
        defaultNotificationShouldNotBeFound("senderLabel.doesNotContain=" + DEFAULT_SENDER_LABEL);

        // Get all the notificationList where senderLabel does not contain UPDATED_SENDER_LABEL
        defaultNotificationShouldBeFound("senderLabel.doesNotContain=" + UPDATED_SENDER_LABEL);
    }


    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt equals to DEFAULT_SEND_AT
        defaultNotificationShouldBeFound("sendAt.equals=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt equals to UPDATED_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.equals=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt not equals to DEFAULT_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.notEquals=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt not equals to UPDATED_SEND_AT
        defaultNotificationShouldBeFound("sendAt.notEquals=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt in DEFAULT_SEND_AT or UPDATED_SEND_AT
        defaultNotificationShouldBeFound("sendAt.in=" + DEFAULT_SEND_AT + "," + UPDATED_SEND_AT);

        // Get all the notificationList where sendAt equals to UPDATED_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.in=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt is not null
        defaultNotificationShouldBeFound("sendAt.specified=true");

        // Get all the notificationList where sendAt is null
        defaultNotificationShouldNotBeFound("sendAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt is greater than or equal to DEFAULT_SEND_AT
        defaultNotificationShouldBeFound("sendAt.greaterThanOrEqual=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt is greater than or equal to UPDATED_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.greaterThanOrEqual=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt is less than or equal to DEFAULT_SEND_AT
        defaultNotificationShouldBeFound("sendAt.lessThanOrEqual=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt is less than or equal to SMALLER_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.lessThanOrEqual=" + SMALLER_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt is less than DEFAULT_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.lessThan=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt is less than UPDATED_SEND_AT
        defaultNotificationShouldBeFound("sendAt.lessThan=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendAt is greater than DEFAULT_SEND_AT
        defaultNotificationShouldNotBeFound("sendAt.greaterThan=" + DEFAULT_SEND_AT);

        // Get all the notificationList where sendAt is greater than SMALLER_SEND_AT
        defaultNotificationShouldBeFound("sendAt.greaterThan=" + SMALLER_SEND_AT);
    }


    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt equals to DEFAULT_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.equals=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt equals to UPDATED_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.equals=" + UPDATED_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt not equals to DEFAULT_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.notEquals=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt not equals to UPDATED_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.notEquals=" + UPDATED_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt in DEFAULT_DECISION_AT or UPDATED_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.in=" + DEFAULT_DECISION_AT + "," + UPDATED_DECISION_AT);

        // Get all the notificationList where decisionAt equals to UPDATED_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.in=" + UPDATED_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt is not null
        defaultNotificationShouldBeFound("decisionAt.specified=true");

        // Get all the notificationList where decisionAt is null
        defaultNotificationShouldNotBeFound("decisionAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt is greater than or equal to DEFAULT_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.greaterThanOrEqual=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt is greater than or equal to UPDATED_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.greaterThanOrEqual=" + UPDATED_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt is less than or equal to DEFAULT_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.lessThanOrEqual=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt is less than or equal to SMALLER_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.lessThanOrEqual=" + SMALLER_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt is less than DEFAULT_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.lessThan=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt is less than UPDATED_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.lessThan=" + UPDATED_DECISION_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDecisionAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where decisionAt is greater than DEFAULT_DECISION_AT
        defaultNotificationShouldNotBeFound("decisionAt.greaterThan=" + DEFAULT_DECISION_AT);

        // Get all the notificationList where decisionAt is greater than SMALLER_DECISION_AT
        defaultNotificationShouldBeFound("decisionAt.greaterThan=" + SMALLER_DECISION_AT);
    }


    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt equals to DEFAULT_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.equals=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt equals to UPDATED_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.equals=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt not equals to DEFAULT_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.notEquals=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt not equals to UPDATED_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.notEquals=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt in DEFAULT_EXPIRE_AT or UPDATED_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.in=" + DEFAULT_EXPIRE_AT + "," + UPDATED_EXPIRE_AT);

        // Get all the notificationList where expireAt equals to UPDATED_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.in=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt is not null
        defaultNotificationShouldBeFound("expireAt.specified=true");

        // Get all the notificationList where expireAt is null
        defaultNotificationShouldNotBeFound("expireAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt is greater than or equal to DEFAULT_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.greaterThanOrEqual=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt is greater than or equal to UPDATED_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.greaterThanOrEqual=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt is less than or equal to DEFAULT_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.lessThanOrEqual=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt is less than or equal to SMALLER_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.lessThanOrEqual=" + SMALLER_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt is less than DEFAULT_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.lessThan=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt is less than UPDATED_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.lessThan=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByExpireAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expireAt is greater than DEFAULT_EXPIRE_AT
        defaultNotificationShouldNotBeFound("expireAt.greaterThan=" + DEFAULT_EXPIRE_AT);

        // Get all the notificationList where expireAt is greater than SMALLER_EXPIRE_AT
        defaultNotificationShouldBeFound("expireAt.greaterThan=" + SMALLER_EXPIRE_AT);
    }


    @Test
    @Transactional
    public void getAllNotificationsByBoxIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        Box box = BoxResourceIT.createEntity(em);
        em.persist(box);
        em.flush();
        notification.setBox(box);
        notificationRepository.saveAndFlush(notification);
        Long boxId = box.getId();

        // Get all the notificationList where box equals to boxId
        defaultNotificationShouldBeFound("boxId.equals=" + boxId);

        // Get all the notificationList where box equals to boxId + 1
        defaultNotificationShouldNotBeFound("boxId.equals=" + (boxId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID)))
            .andExpect(jsonPath("$.[*].senderLabel").value(hasItem(DEFAULT_SENDER_LABEL)))
            .andExpect(jsonPath("$.[*].sendAt").value(hasItem(sameInstant(DEFAULT_SEND_AT))))
            .andExpect(jsonPath("$.[*].decisionAt").value(hasItem(sameInstant(DEFAULT_DECISION_AT))))
            .andExpect(jsonPath("$.[*].expireAt").value(hasItem(sameInstant(DEFAULT_EXPIRE_AT))));

        // Check, that the count call also returns 1
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID)
            .processId(UPDATED_PROCESS_ID)
            .subject(UPDATED_SUBJECT)
            .state(UPDATED_STATE)
            .senderId(UPDATED_SENDER_ID)
            .senderLabel(UPDATED_SENDER_LABEL)
            .sendAt(UPDATED_SEND_AT)
            .decisionAt(UPDATED_DECISION_AT)
            .expireAt(UPDATED_EXPIRE_AT);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc.perform(put("/api/notifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getEan()).isEqualTo(UPDATED_EAN);
        assertThat(testNotification.getErdsId()).isEqualTo(UPDATED_ERDS_ID);
        assertThat(testNotification.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testNotification.getSenderId()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testNotification.getSenderLabel()).isEqualTo(UPDATED_SENDER_LABEL);
        assertThat(testNotification.getSendAt()).isEqualTo(UPDATED_SEND_AT);
        assertThat(testNotification.getDecisionAt()).isEqualTo(UPDATED_DECISION_AT);
        assertThat(testNotification.getExpireAt()).isEqualTo(UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
