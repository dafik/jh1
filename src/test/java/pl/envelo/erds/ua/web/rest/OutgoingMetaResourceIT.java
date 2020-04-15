package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.OutgoingMeta;
import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.repository.OutgoingMetaRepository;
import pl.envelo.erds.ua.service.OutgoingMetaService;
import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;
import pl.envelo.erds.ua.service.mapper.OutgoingMetaMapper;
import pl.envelo.erds.ua.service.dto.OutgoingMetaCriteria;
import pl.envelo.erds.ua.service.OutgoingMetaQueryService;

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

/**
 * Integration tests for the {@link OutgoingMetaResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class OutgoingMetaResourceIT {

    private static final ZonedDateTime DEFAULT_SEND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SEND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_EXPIRE_AT = 1;
    private static final Integer UPDATED_EXPIRE_AT = 2;
    private static final Integer SMALLER_EXPIRE_AT = 1 - 1;

    private static final ZonedDateTime DEFAULT_LAST_EDIT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_EDIT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_EDIT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private OutgoingMetaRepository outgoingMetaRepository;

    @Autowired
    private OutgoingMetaMapper outgoingMetaMapper;

    @Autowired
    private OutgoingMetaService outgoingMetaService;

    @Autowired
    private OutgoingMetaQueryService outgoingMetaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutgoingMetaMockMvc;

    private OutgoingMeta outgoingMeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutgoingMeta createEntity(EntityManager em) {
        OutgoingMeta outgoingMeta = new OutgoingMeta()
            .sendAt(DEFAULT_SEND_AT)
            .expireAt(DEFAULT_EXPIRE_AT)
            .lastEditAt(DEFAULT_LAST_EDIT_AT);
        return outgoingMeta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutgoingMeta createUpdatedEntity(EntityManager em) {
        OutgoingMeta outgoingMeta = new OutgoingMeta()
            .sendAt(UPDATED_SEND_AT)
            .expireAt(UPDATED_EXPIRE_AT)
            .lastEditAt(UPDATED_LAST_EDIT_AT);
        return outgoingMeta;
    }

    @BeforeEach
    public void initTest() {
        outgoingMeta = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutgoingMeta() throws Exception {
        int databaseSizeBeforeCreate = outgoingMetaRepository.findAll().size();

        // Create the OutgoingMeta
        OutgoingMetaDTO outgoingMetaDTO = outgoingMetaMapper.toDto(outgoingMeta);
        restOutgoingMetaMockMvc.perform(post("/api/outgoing-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(outgoingMetaDTO)))
            .andExpect(status().isCreated());

        // Validate the OutgoingMeta in the database
        List<OutgoingMeta> outgoingMetaList = outgoingMetaRepository.findAll();
        assertThat(outgoingMetaList).hasSize(databaseSizeBeforeCreate + 1);
        OutgoingMeta testOutgoingMeta = outgoingMetaList.get(outgoingMetaList.size() - 1);
        assertThat(testOutgoingMeta.getSendAt()).isEqualTo(DEFAULT_SEND_AT);
        assertThat(testOutgoingMeta.getExpireAt()).isEqualTo(DEFAULT_EXPIRE_AT);
        assertThat(testOutgoingMeta.getLastEditAt()).isEqualTo(DEFAULT_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void createOutgoingMetaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outgoingMetaRepository.findAll().size();

        // Create the OutgoingMeta with an existing ID
        outgoingMeta.setId(1L);
        OutgoingMetaDTO outgoingMetaDTO = outgoingMetaMapper.toDto(outgoingMeta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutgoingMetaMockMvc.perform(post("/api/outgoing-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(outgoingMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OutgoingMeta in the database
        List<OutgoingMeta> outgoingMetaList = outgoingMetaRepository.findAll();
        assertThat(outgoingMetaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOutgoingMetas() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outgoingMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendAt").value(hasItem(sameInstant(DEFAULT_SEND_AT))))
            .andExpect(jsonPath("$.[*].expireAt").value(hasItem(DEFAULT_EXPIRE_AT)))
            .andExpect(jsonPath("$.[*].lastEditAt").value(hasItem(sameInstant(DEFAULT_LAST_EDIT_AT))));
    }
    
    @Test
    @Transactional
    public void getOutgoingMeta() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get the outgoingMeta
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas/{id}", outgoingMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outgoingMeta.getId().intValue()))
            .andExpect(jsonPath("$.sendAt").value(sameInstant(DEFAULT_SEND_AT)))
            .andExpect(jsonPath("$.expireAt").value(DEFAULT_EXPIRE_AT))
            .andExpect(jsonPath("$.lastEditAt").value(sameInstant(DEFAULT_LAST_EDIT_AT)));
    }


    @Test
    @Transactional
    public void getOutgoingMetasByIdFiltering() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        Long id = outgoingMeta.getId();

        defaultOutgoingMetaShouldBeFound("id.equals=" + id);
        defaultOutgoingMetaShouldNotBeFound("id.notEquals=" + id);

        defaultOutgoingMetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutgoingMetaShouldNotBeFound("id.greaterThan=" + id);

        defaultOutgoingMetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutgoingMetaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt equals to DEFAULT_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.equals=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt equals to UPDATED_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.equals=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt not equals to DEFAULT_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.notEquals=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt not equals to UPDATED_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.notEquals=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsInShouldWork() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt in DEFAULT_SEND_AT or UPDATED_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.in=" + DEFAULT_SEND_AT + "," + UPDATED_SEND_AT);

        // Get all the outgoingMetaList where sendAt equals to UPDATED_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.in=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt is not null
        defaultOutgoingMetaShouldBeFound("sendAt.specified=true");

        // Get all the outgoingMetaList where sendAt is null
        defaultOutgoingMetaShouldNotBeFound("sendAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt is greater than or equal to DEFAULT_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.greaterThanOrEqual=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt is greater than or equal to UPDATED_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.greaterThanOrEqual=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt is less than or equal to DEFAULT_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.lessThanOrEqual=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt is less than or equal to SMALLER_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.lessThanOrEqual=" + SMALLER_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsLessThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt is less than DEFAULT_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.lessThan=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt is less than UPDATED_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.lessThan=" + UPDATED_SEND_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasBySendAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where sendAt is greater than DEFAULT_SEND_AT
        defaultOutgoingMetaShouldNotBeFound("sendAt.greaterThan=" + DEFAULT_SEND_AT);

        // Get all the outgoingMetaList where sendAt is greater than SMALLER_SEND_AT
        defaultOutgoingMetaShouldBeFound("sendAt.greaterThan=" + SMALLER_SEND_AT);
    }


    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt equals to DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.equals=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt equals to UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.equals=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt not equals to DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.notEquals=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt not equals to UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.notEquals=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsInShouldWork() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt in DEFAULT_EXPIRE_AT or UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.in=" + DEFAULT_EXPIRE_AT + "," + UPDATED_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt equals to UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.in=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt is not null
        defaultOutgoingMetaShouldBeFound("expireAt.specified=true");

        // Get all the outgoingMetaList where expireAt is null
        defaultOutgoingMetaShouldNotBeFound("expireAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt is greater than or equal to DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.greaterThanOrEqual=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt is greater than or equal to UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.greaterThanOrEqual=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt is less than or equal to DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.lessThanOrEqual=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt is less than or equal to SMALLER_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.lessThanOrEqual=" + SMALLER_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsLessThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt is less than DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.lessThan=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt is less than UPDATED_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.lessThan=" + UPDATED_EXPIRE_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByExpireAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where expireAt is greater than DEFAULT_EXPIRE_AT
        defaultOutgoingMetaShouldNotBeFound("expireAt.greaterThan=" + DEFAULT_EXPIRE_AT);

        // Get all the outgoingMetaList where expireAt is greater than SMALLER_EXPIRE_AT
        defaultOutgoingMetaShouldBeFound("expireAt.greaterThan=" + SMALLER_EXPIRE_AT);
    }


    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt equals to DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.equals=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt equals to UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.equals=" + UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt not equals to DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.notEquals=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt not equals to UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.notEquals=" + UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsInShouldWork() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt in DEFAULT_LAST_EDIT_AT or UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.in=" + DEFAULT_LAST_EDIT_AT + "," + UPDATED_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt equals to UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.in=" + UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt is not null
        defaultOutgoingMetaShouldBeFound("lastEditAt.specified=true");

        // Get all the outgoingMetaList where lastEditAt is null
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt is greater than or equal to DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.greaterThanOrEqual=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt is greater than or equal to UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.greaterThanOrEqual=" + UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt is less than or equal to DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.lessThanOrEqual=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt is less than or equal to SMALLER_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.lessThanOrEqual=" + SMALLER_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsLessThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt is less than DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.lessThan=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt is less than UPDATED_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.lessThan=" + UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllOutgoingMetasByLastEditAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        // Get all the outgoingMetaList where lastEditAt is greater than DEFAULT_LAST_EDIT_AT
        defaultOutgoingMetaShouldNotBeFound("lastEditAt.greaterThan=" + DEFAULT_LAST_EDIT_AT);

        // Get all the outgoingMetaList where lastEditAt is greater than SMALLER_LAST_EDIT_AT
        defaultOutgoingMetaShouldBeFound("lastEditAt.greaterThan=" + SMALLER_LAST_EDIT_AT);
    }


    @Test
    @Transactional
    public void getAllOutgoingMetasByLetterGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);
        LetterGroup letterGroup = LetterGroupResourceIT.createEntity(em);
        em.persist(letterGroup);
        em.flush();
        outgoingMeta.setLetterGroup(letterGroup);
        letterGroup.setOutgoingMeta(outgoingMeta);
        outgoingMetaRepository.saveAndFlush(outgoingMeta);
        Long letterGroupId = letterGroup.getId();

        // Get all the outgoingMetaList where letterGroup equals to letterGroupId
        defaultOutgoingMetaShouldBeFound("letterGroupId.equals=" + letterGroupId);

        // Get all the outgoingMetaList where letterGroup equals to letterGroupId + 1
        defaultOutgoingMetaShouldNotBeFound("letterGroupId.equals=" + (letterGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutgoingMetaShouldBeFound(String filter) throws Exception {
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outgoingMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendAt").value(hasItem(sameInstant(DEFAULT_SEND_AT))))
            .andExpect(jsonPath("$.[*].expireAt").value(hasItem(DEFAULT_EXPIRE_AT)))
            .andExpect(jsonPath("$.[*].lastEditAt").value(hasItem(sameInstant(DEFAULT_LAST_EDIT_AT))));

        // Check, that the count call also returns 1
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutgoingMetaShouldNotBeFound(String filter) throws Exception {
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOutgoingMeta() throws Exception {
        // Get the outgoingMeta
        restOutgoingMetaMockMvc.perform(get("/api/outgoing-metas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutgoingMeta() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        int databaseSizeBeforeUpdate = outgoingMetaRepository.findAll().size();

        // Update the outgoingMeta
        OutgoingMeta updatedOutgoingMeta = outgoingMetaRepository.findById(outgoingMeta.getId()).get();
        // Disconnect from session so that the updates on updatedOutgoingMeta are not directly saved in db
        em.detach(updatedOutgoingMeta);
        updatedOutgoingMeta
            .sendAt(UPDATED_SEND_AT)
            .expireAt(UPDATED_EXPIRE_AT)
            .lastEditAt(UPDATED_LAST_EDIT_AT);
        OutgoingMetaDTO outgoingMetaDTO = outgoingMetaMapper.toDto(updatedOutgoingMeta);

        restOutgoingMetaMockMvc.perform(put("/api/outgoing-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(outgoingMetaDTO)))
            .andExpect(status().isOk());

        // Validate the OutgoingMeta in the database
        List<OutgoingMeta> outgoingMetaList = outgoingMetaRepository.findAll();
        assertThat(outgoingMetaList).hasSize(databaseSizeBeforeUpdate);
        OutgoingMeta testOutgoingMeta = outgoingMetaList.get(outgoingMetaList.size() - 1);
        assertThat(testOutgoingMeta.getSendAt()).isEqualTo(UPDATED_SEND_AT);
        assertThat(testOutgoingMeta.getExpireAt()).isEqualTo(UPDATED_EXPIRE_AT);
        assertThat(testOutgoingMeta.getLastEditAt()).isEqualTo(UPDATED_LAST_EDIT_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingOutgoingMeta() throws Exception {
        int databaseSizeBeforeUpdate = outgoingMetaRepository.findAll().size();

        // Create the OutgoingMeta
        OutgoingMetaDTO outgoingMetaDTO = outgoingMetaMapper.toDto(outgoingMeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutgoingMetaMockMvc.perform(put("/api/outgoing-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(outgoingMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OutgoingMeta in the database
        List<OutgoingMeta> outgoingMetaList = outgoingMetaRepository.findAll();
        assertThat(outgoingMetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOutgoingMeta() throws Exception {
        // Initialize the database
        outgoingMetaRepository.saveAndFlush(outgoingMeta);

        int databaseSizeBeforeDelete = outgoingMetaRepository.findAll().size();

        // Delete the outgoingMeta
        restOutgoingMetaMockMvc.perform(delete("/api/outgoing-metas/{id}", outgoingMeta.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutgoingMeta> outgoingMetaList = outgoingMetaRepository.findAll();
        assertThat(outgoingMetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
