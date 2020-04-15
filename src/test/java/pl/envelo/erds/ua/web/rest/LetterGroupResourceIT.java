package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.domain.IncomingMeta;
import pl.envelo.erds.ua.domain.OutgoingMeta;
import pl.envelo.erds.ua.domain.Attachment;
import pl.envelo.erds.ua.domain.Letter;
import pl.envelo.erds.ua.domain.Box;
import pl.envelo.erds.ua.domain.Thread;
import pl.envelo.erds.ua.repository.LetterGroupRepository;
import pl.envelo.erds.ua.service.LetterGroupService;
import pl.envelo.erds.ua.service.dto.LetterGroupDTO;
import pl.envelo.erds.ua.service.mapper.LetterGroupMapper;
import pl.envelo.erds.ua.service.dto.LetterGroupCriteria;
import pl.envelo.erds.ua.service.LetterGroupQueryService;

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

import pl.envelo.erds.ua.domain.enumeration.LetterState;
/**
 * Integration tests for the {@link LetterGroupResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class LetterGroupResourceIT {

    private static final String DEFAULT_MSG_ID = "AAAAAAAAAA";
    private static final String UPDATED_MSG_ID = "BBBBBBBBBB";

    private static final LetterState DEFAULT_STATE = LetterState.RECEIVED;
    private static final LetterState UPDATED_STATE = LetterState.TRASH_RECEIVED;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_REPLAY_TO = "AAAAAAAAAA";
    private static final String UPDATED_REPLAY_TO = "BBBBBBBBBB";

    private static final String DEFAULT_IN_REPLAY_TO = "AAAAAAAAAA";
    private static final String UPDATED_IN_REPLAY_TO = "BBBBBBBBBB";

    @Autowired
    private LetterGroupRepository letterGroupRepository;

    @Autowired
    private LetterGroupMapper letterGroupMapper;

    @Autowired
    private LetterGroupService letterGroupService;

    @Autowired
    private LetterGroupQueryService letterGroupQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLetterGroupMockMvc;

    private LetterGroup letterGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LetterGroup createEntity(EntityManager em) {
        LetterGroup letterGroup = new LetterGroup()
            .msgId(DEFAULT_MSG_ID)
            .state(DEFAULT_STATE)
            .subject(DEFAULT_SUBJECT)
            .createdAt(DEFAULT_CREATED_AT)
            .replayTo(DEFAULT_REPLAY_TO)
            .inReplayTo(DEFAULT_IN_REPLAY_TO);
        return letterGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LetterGroup createUpdatedEntity(EntityManager em) {
        LetterGroup letterGroup = new LetterGroup()
            .msgId(UPDATED_MSG_ID)
            .state(UPDATED_STATE)
            .subject(UPDATED_SUBJECT)
            .createdAt(UPDATED_CREATED_AT)
            .replayTo(UPDATED_REPLAY_TO)
            .inReplayTo(UPDATED_IN_REPLAY_TO);
        return letterGroup;
    }

    @BeforeEach
    public void initTest() {
        letterGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createLetterGroup() throws Exception {
        int databaseSizeBeforeCreate = letterGroupRepository.findAll().size();

        // Create the LetterGroup
        LetterGroupDTO letterGroupDTO = letterGroupMapper.toDto(letterGroup);
        restLetterGroupMockMvc.perform(post("/api/letter-groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the LetterGroup in the database
        List<LetterGroup> letterGroupList = letterGroupRepository.findAll();
        assertThat(letterGroupList).hasSize(databaseSizeBeforeCreate + 1);
        LetterGroup testLetterGroup = letterGroupList.get(letterGroupList.size() - 1);
        assertThat(testLetterGroup.getMsgId()).isEqualTo(DEFAULT_MSG_ID);
        assertThat(testLetterGroup.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLetterGroup.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testLetterGroup.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLetterGroup.getReplayTo()).isEqualTo(DEFAULT_REPLAY_TO);
        assertThat(testLetterGroup.getInReplayTo()).isEqualTo(DEFAULT_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void createLetterGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = letterGroupRepository.findAll().size();

        // Create the LetterGroup with an existing ID
        letterGroup.setId(1L);
        LetterGroupDTO letterGroupDTO = letterGroupMapper.toDto(letterGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLetterGroupMockMvc.perform(post("/api/letter-groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LetterGroup in the database
        List<LetterGroup> letterGroupList = letterGroupRepository.findAll();
        assertThat(letterGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLetterGroups() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList
        restLetterGroupMockMvc.perform(get("/api/letter-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(letterGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].msgId").value(hasItem(DEFAULT_MSG_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].replayTo").value(hasItem(DEFAULT_REPLAY_TO)))
            .andExpect(jsonPath("$.[*].inReplayTo").value(hasItem(DEFAULT_IN_REPLAY_TO)));
    }
    
    @Test
    @Transactional
    public void getLetterGroup() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get the letterGroup
        restLetterGroupMockMvc.perform(get("/api/letter-groups/{id}", letterGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(letterGroup.getId().intValue()))
            .andExpect(jsonPath("$.msgId").value(DEFAULT_MSG_ID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.replayTo").value(DEFAULT_REPLAY_TO))
            .andExpect(jsonPath("$.inReplayTo").value(DEFAULT_IN_REPLAY_TO));
    }


    @Test
    @Transactional
    public void getLetterGroupsByIdFiltering() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        Long id = letterGroup.getId();

        defaultLetterGroupShouldBeFound("id.equals=" + id);
        defaultLetterGroupShouldNotBeFound("id.notEquals=" + id);

        defaultLetterGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLetterGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultLetterGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLetterGroupShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId equals to DEFAULT_MSG_ID
        defaultLetterGroupShouldBeFound("msgId.equals=" + DEFAULT_MSG_ID);

        // Get all the letterGroupList where msgId equals to UPDATED_MSG_ID
        defaultLetterGroupShouldNotBeFound("msgId.equals=" + UPDATED_MSG_ID);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId not equals to DEFAULT_MSG_ID
        defaultLetterGroupShouldNotBeFound("msgId.notEquals=" + DEFAULT_MSG_ID);

        // Get all the letterGroupList where msgId not equals to UPDATED_MSG_ID
        defaultLetterGroupShouldBeFound("msgId.notEquals=" + UPDATED_MSG_ID);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId in DEFAULT_MSG_ID or UPDATED_MSG_ID
        defaultLetterGroupShouldBeFound("msgId.in=" + DEFAULT_MSG_ID + "," + UPDATED_MSG_ID);

        // Get all the letterGroupList where msgId equals to UPDATED_MSG_ID
        defaultLetterGroupShouldNotBeFound("msgId.in=" + UPDATED_MSG_ID);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId is not null
        defaultLetterGroupShouldBeFound("msgId.specified=true");

        // Get all the letterGroupList where msgId is null
        defaultLetterGroupShouldNotBeFound("msgId.specified=false");
    }
                @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId contains DEFAULT_MSG_ID
        defaultLetterGroupShouldBeFound("msgId.contains=" + DEFAULT_MSG_ID);

        // Get all the letterGroupList where msgId contains UPDATED_MSG_ID
        defaultLetterGroupShouldNotBeFound("msgId.contains=" + UPDATED_MSG_ID);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByMsgIdNotContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where msgId does not contain DEFAULT_MSG_ID
        defaultLetterGroupShouldNotBeFound("msgId.doesNotContain=" + DEFAULT_MSG_ID);

        // Get all the letterGroupList where msgId does not contain UPDATED_MSG_ID
        defaultLetterGroupShouldBeFound("msgId.doesNotContain=" + UPDATED_MSG_ID);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where state equals to DEFAULT_STATE
        defaultLetterGroupShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the letterGroupList where state equals to UPDATED_STATE
        defaultLetterGroupShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where state not equals to DEFAULT_STATE
        defaultLetterGroupShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the letterGroupList where state not equals to UPDATED_STATE
        defaultLetterGroupShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where state in DEFAULT_STATE or UPDATED_STATE
        defaultLetterGroupShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the letterGroupList where state equals to UPDATED_STATE
        defaultLetterGroupShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where state is not null
        defaultLetterGroupShouldBeFound("state.specified=true");

        // Get all the letterGroupList where state is null
        defaultLetterGroupShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllLetterGroupsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject equals to DEFAULT_SUBJECT
        defaultLetterGroupShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the letterGroupList where subject equals to UPDATED_SUBJECT
        defaultLetterGroupShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject not equals to DEFAULT_SUBJECT
        defaultLetterGroupShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the letterGroupList where subject not equals to UPDATED_SUBJECT
        defaultLetterGroupShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultLetterGroupShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the letterGroupList where subject equals to UPDATED_SUBJECT
        defaultLetterGroupShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject is not null
        defaultLetterGroupShouldBeFound("subject.specified=true");

        // Get all the letterGroupList where subject is null
        defaultLetterGroupShouldNotBeFound("subject.specified=false");
    }
                @Test
    @Transactional
    public void getAllLetterGroupsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject contains DEFAULT_SUBJECT
        defaultLetterGroupShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the letterGroupList where subject contains UPDATED_SUBJECT
        defaultLetterGroupShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where subject does not contain DEFAULT_SUBJECT
        defaultLetterGroupShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the letterGroupList where subject does not contain UPDATED_SUBJECT
        defaultLetterGroupShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt equals to DEFAULT_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt equals to UPDATED_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt not equals to DEFAULT_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt not equals to UPDATED_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the letterGroupList where createdAt equals to UPDATED_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt is not null
        defaultLetterGroupShouldBeFound("createdAt.specified=true");

        // Get all the letterGroupList where createdAt is null
        defaultLetterGroupShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt is less than DEFAULT_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt is less than UPDATED_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where createdAt is greater than DEFAULT_CREATED_AT
        defaultLetterGroupShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the letterGroupList where createdAt is greater than SMALLER_CREATED_AT
        defaultLetterGroupShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByReplayToIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo equals to DEFAULT_REPLAY_TO
        defaultLetterGroupShouldBeFound("replayTo.equals=" + DEFAULT_REPLAY_TO);

        // Get all the letterGroupList where replayTo equals to UPDATED_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("replayTo.equals=" + UPDATED_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByReplayToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo not equals to DEFAULT_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("replayTo.notEquals=" + DEFAULT_REPLAY_TO);

        // Get all the letterGroupList where replayTo not equals to UPDATED_REPLAY_TO
        defaultLetterGroupShouldBeFound("replayTo.notEquals=" + UPDATED_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByReplayToIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo in DEFAULT_REPLAY_TO or UPDATED_REPLAY_TO
        defaultLetterGroupShouldBeFound("replayTo.in=" + DEFAULT_REPLAY_TO + "," + UPDATED_REPLAY_TO);

        // Get all the letterGroupList where replayTo equals to UPDATED_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("replayTo.in=" + UPDATED_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByReplayToIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo is not null
        defaultLetterGroupShouldBeFound("replayTo.specified=true");

        // Get all the letterGroupList where replayTo is null
        defaultLetterGroupShouldNotBeFound("replayTo.specified=false");
    }
                @Test
    @Transactional
    public void getAllLetterGroupsByReplayToContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo contains DEFAULT_REPLAY_TO
        defaultLetterGroupShouldBeFound("replayTo.contains=" + DEFAULT_REPLAY_TO);

        // Get all the letterGroupList where replayTo contains UPDATED_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("replayTo.contains=" + UPDATED_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByReplayToNotContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where replayTo does not contain DEFAULT_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("replayTo.doesNotContain=" + DEFAULT_REPLAY_TO);

        // Get all the letterGroupList where replayTo does not contain UPDATED_REPLAY_TO
        defaultLetterGroupShouldBeFound("replayTo.doesNotContain=" + UPDATED_REPLAY_TO);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo equals to DEFAULT_IN_REPLAY_TO
        defaultLetterGroupShouldBeFound("inReplayTo.equals=" + DEFAULT_IN_REPLAY_TO);

        // Get all the letterGroupList where inReplayTo equals to UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("inReplayTo.equals=" + UPDATED_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo not equals to DEFAULT_IN_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("inReplayTo.notEquals=" + DEFAULT_IN_REPLAY_TO);

        // Get all the letterGroupList where inReplayTo not equals to UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldBeFound("inReplayTo.notEquals=" + UPDATED_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToIsInShouldWork() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo in DEFAULT_IN_REPLAY_TO or UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldBeFound("inReplayTo.in=" + DEFAULT_IN_REPLAY_TO + "," + UPDATED_IN_REPLAY_TO);

        // Get all the letterGroupList where inReplayTo equals to UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("inReplayTo.in=" + UPDATED_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo is not null
        defaultLetterGroupShouldBeFound("inReplayTo.specified=true");

        // Get all the letterGroupList where inReplayTo is null
        defaultLetterGroupShouldNotBeFound("inReplayTo.specified=false");
    }
                @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo contains DEFAULT_IN_REPLAY_TO
        defaultLetterGroupShouldBeFound("inReplayTo.contains=" + DEFAULT_IN_REPLAY_TO);

        // Get all the letterGroupList where inReplayTo contains UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("inReplayTo.contains=" + UPDATED_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void getAllLetterGroupsByInReplayToNotContainsSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        // Get all the letterGroupList where inReplayTo does not contain DEFAULT_IN_REPLAY_TO
        defaultLetterGroupShouldNotBeFound("inReplayTo.doesNotContain=" + DEFAULT_IN_REPLAY_TO);

        // Get all the letterGroupList where inReplayTo does not contain UPDATED_IN_REPLAY_TO
        defaultLetterGroupShouldBeFound("inReplayTo.doesNotContain=" + UPDATED_IN_REPLAY_TO);
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByIncomingMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        IncomingMeta incomingMeta = IncomingMetaResourceIT.createEntity(em);
        em.persist(incomingMeta);
        em.flush();
        letterGroup.setIncomingMeta(incomingMeta);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long incomingMetaId = incomingMeta.getId();

        // Get all the letterGroupList where incomingMeta equals to incomingMetaId
        defaultLetterGroupShouldBeFound("incomingMetaId.equals=" + incomingMetaId);

        // Get all the letterGroupList where incomingMeta equals to incomingMetaId + 1
        defaultLetterGroupShouldNotBeFound("incomingMetaId.equals=" + (incomingMetaId + 1));
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByOutgoingMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        OutgoingMeta outgoingMeta = OutgoingMetaResourceIT.createEntity(em);
        em.persist(outgoingMeta);
        em.flush();
        letterGroup.setOutgoingMeta(outgoingMeta);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long outgoingMetaId = outgoingMeta.getId();

        // Get all the letterGroupList where outgoingMeta equals to outgoingMetaId
        defaultLetterGroupShouldBeFound("outgoingMetaId.equals=" + outgoingMetaId);

        // Get all the letterGroupList where outgoingMeta equals to outgoingMetaId + 1
        defaultLetterGroupShouldNotBeFound("outgoingMetaId.equals=" + (outgoingMetaId + 1));
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        Attachment attachment = AttachmentResourceIT.createEntity(em);
        em.persist(attachment);
        em.flush();
        letterGroup.addAttachment(attachment);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long attachmentId = attachment.getId();

        // Get all the letterGroupList where attachment equals to attachmentId
        defaultLetterGroupShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the letterGroupList where attachment equals to attachmentId + 1
        defaultLetterGroupShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByLetterIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        Letter letter = LetterResourceIT.createEntity(em);
        em.persist(letter);
        em.flush();
        letterGroup.addLetter(letter);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long letterId = letter.getId();

        // Get all the letterGroupList where letter equals to letterId
        defaultLetterGroupShouldBeFound("letterId.equals=" + letterId);

        // Get all the letterGroupList where letter equals to letterId + 1
        defaultLetterGroupShouldNotBeFound("letterId.equals=" + (letterId + 1));
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByBoxIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        Box box = BoxResourceIT.createEntity(em);
        em.persist(box);
        em.flush();
        letterGroup.setBox(box);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long boxId = box.getId();

        // Get all the letterGroupList where box equals to boxId
        defaultLetterGroupShouldBeFound("boxId.equals=" + boxId);

        // Get all the letterGroupList where box equals to boxId + 1
        defaultLetterGroupShouldNotBeFound("boxId.equals=" + (boxId + 1));
    }


    @Test
    @Transactional
    public void getAllLetterGroupsByThreadIsEqualToSomething() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);
        Thread thread = ThreadResourceIT.createEntity(em);
        em.persist(thread);
        em.flush();
        letterGroup.setThread(thread);
        letterGroupRepository.saveAndFlush(letterGroup);
        Long threadId = thread.getId();

        // Get all the letterGroupList where thread equals to threadId
        defaultLetterGroupShouldBeFound("threadId.equals=" + threadId);

        // Get all the letterGroupList where thread equals to threadId + 1
        defaultLetterGroupShouldNotBeFound("threadId.equals=" + (threadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLetterGroupShouldBeFound(String filter) throws Exception {
        restLetterGroupMockMvc.perform(get("/api/letter-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(letterGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].msgId").value(hasItem(DEFAULT_MSG_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].replayTo").value(hasItem(DEFAULT_REPLAY_TO)))
            .andExpect(jsonPath("$.[*].inReplayTo").value(hasItem(DEFAULT_IN_REPLAY_TO)));

        // Check, that the count call also returns 1
        restLetterGroupMockMvc.perform(get("/api/letter-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLetterGroupShouldNotBeFound(String filter) throws Exception {
        restLetterGroupMockMvc.perform(get("/api/letter-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLetterGroupMockMvc.perform(get("/api/letter-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLetterGroup() throws Exception {
        // Get the letterGroup
        restLetterGroupMockMvc.perform(get("/api/letter-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLetterGroup() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        int databaseSizeBeforeUpdate = letterGroupRepository.findAll().size();

        // Update the letterGroup
        LetterGroup updatedLetterGroup = letterGroupRepository.findById(letterGroup.getId()).get();
        // Disconnect from session so that the updates on updatedLetterGroup are not directly saved in db
        em.detach(updatedLetterGroup);
        updatedLetterGroup
            .msgId(UPDATED_MSG_ID)
            .state(UPDATED_STATE)
            .subject(UPDATED_SUBJECT)
            .createdAt(UPDATED_CREATED_AT)
            .replayTo(UPDATED_REPLAY_TO)
            .inReplayTo(UPDATED_IN_REPLAY_TO);
        LetterGroupDTO letterGroupDTO = letterGroupMapper.toDto(updatedLetterGroup);

        restLetterGroupMockMvc.perform(put("/api/letter-groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterGroupDTO)))
            .andExpect(status().isOk());

        // Validate the LetterGroup in the database
        List<LetterGroup> letterGroupList = letterGroupRepository.findAll();
        assertThat(letterGroupList).hasSize(databaseSizeBeforeUpdate);
        LetterGroup testLetterGroup = letterGroupList.get(letterGroupList.size() - 1);
        assertThat(testLetterGroup.getMsgId()).isEqualTo(UPDATED_MSG_ID);
        assertThat(testLetterGroup.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLetterGroup.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testLetterGroup.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLetterGroup.getReplayTo()).isEqualTo(UPDATED_REPLAY_TO);
        assertThat(testLetterGroup.getInReplayTo()).isEqualTo(UPDATED_IN_REPLAY_TO);
    }

    @Test
    @Transactional
    public void updateNonExistingLetterGroup() throws Exception {
        int databaseSizeBeforeUpdate = letterGroupRepository.findAll().size();

        // Create the LetterGroup
        LetterGroupDTO letterGroupDTO = letterGroupMapper.toDto(letterGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLetterGroupMockMvc.perform(put("/api/letter-groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LetterGroup in the database
        List<LetterGroup> letterGroupList = letterGroupRepository.findAll();
        assertThat(letterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLetterGroup() throws Exception {
        // Initialize the database
        letterGroupRepository.saveAndFlush(letterGroup);

        int databaseSizeBeforeDelete = letterGroupRepository.findAll().size();

        // Delete the letterGroup
        restLetterGroupMockMvc.perform(delete("/api/letter-groups/{id}", letterGroup.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LetterGroup> letterGroupList = letterGroupRepository.findAll();
        assertThat(letterGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
