package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.IncomingMeta;
import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.repository.IncomingMetaRepository;
import pl.envelo.erds.ua.service.IncomingMetaService;
import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;
import pl.envelo.erds.ua.service.mapper.IncomingMetaMapper;
import pl.envelo.erds.ua.service.dto.IncomingMetaCriteria;
import pl.envelo.erds.ua.service.IncomingMetaQueryService;

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
 * Integration tests for the {@link IncomingMetaResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class IncomingMetaResourceIT {

    private static final ZonedDateTime DEFAULT_RECEIVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECEIVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RECEIVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_READ_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READ_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_READ_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private IncomingMetaRepository incomingMetaRepository;

    @Autowired
    private IncomingMetaMapper incomingMetaMapper;

    @Autowired
    private IncomingMetaService incomingMetaService;

    @Autowired
    private IncomingMetaQueryService incomingMetaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomingMetaMockMvc;

    private IncomingMeta incomingMeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomingMeta createEntity(EntityManager em) {
        IncomingMeta incomingMeta = new IncomingMeta()
            .receivedAt(DEFAULT_RECEIVED_AT)
            .readAt(DEFAULT_READ_AT);
        return incomingMeta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomingMeta createUpdatedEntity(EntityManager em) {
        IncomingMeta incomingMeta = new IncomingMeta()
            .receivedAt(UPDATED_RECEIVED_AT)
            .readAt(UPDATED_READ_AT);
        return incomingMeta;
    }

    @BeforeEach
    public void initTest() {
        incomingMeta = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncomingMeta() throws Exception {
        int databaseSizeBeforeCreate = incomingMetaRepository.findAll().size();

        // Create the IncomingMeta
        IncomingMetaDTO incomingMetaDTO = incomingMetaMapper.toDto(incomingMeta);
        restIncomingMetaMockMvc.perform(post("/api/incoming-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomingMetaDTO)))
            .andExpect(status().isCreated());

        // Validate the IncomingMeta in the database
        List<IncomingMeta> incomingMetaList = incomingMetaRepository.findAll();
        assertThat(incomingMetaList).hasSize(databaseSizeBeforeCreate + 1);
        IncomingMeta testIncomingMeta = incomingMetaList.get(incomingMetaList.size() - 1);
        assertThat(testIncomingMeta.getReceivedAt()).isEqualTo(DEFAULT_RECEIVED_AT);
        assertThat(testIncomingMeta.getReadAt()).isEqualTo(DEFAULT_READ_AT);
    }

    @Test
    @Transactional
    public void createIncomingMetaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incomingMetaRepository.findAll().size();

        // Create the IncomingMeta with an existing ID
        incomingMeta.setId(1L);
        IncomingMetaDTO incomingMetaDTO = incomingMetaMapper.toDto(incomingMeta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomingMetaMockMvc.perform(post("/api/incoming-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomingMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IncomingMeta in the database
        List<IncomingMeta> incomingMetaList = incomingMetaRepository.findAll();
        assertThat(incomingMetaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllIncomingMetas() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomingMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(sameInstant(DEFAULT_RECEIVED_AT))))
            .andExpect(jsonPath("$.[*].readAt").value(hasItem(sameInstant(DEFAULT_READ_AT))));
    }
    
    @Test
    @Transactional
    public void getIncomingMeta() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get the incomingMeta
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas/{id}", incomingMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomingMeta.getId().intValue()))
            .andExpect(jsonPath("$.receivedAt").value(sameInstant(DEFAULT_RECEIVED_AT)))
            .andExpect(jsonPath("$.readAt").value(sameInstant(DEFAULT_READ_AT)));
    }


    @Test
    @Transactional
    public void getIncomingMetasByIdFiltering() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        Long id = incomingMeta.getId();

        defaultIncomingMetaShouldBeFound("id.equals=" + id);
        defaultIncomingMetaShouldNotBeFound("id.notEquals=" + id);

        defaultIncomingMetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIncomingMetaShouldNotBeFound("id.greaterThan=" + id);

        defaultIncomingMetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIncomingMetaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt equals to DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.equals=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt equals to UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.equals=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt not equals to DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.notEquals=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt not equals to UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.notEquals=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsInShouldWork() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt in DEFAULT_RECEIVED_AT or UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.in=" + DEFAULT_RECEIVED_AT + "," + UPDATED_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt equals to UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.in=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt is not null
        defaultIncomingMetaShouldBeFound("receivedAt.specified=true");

        // Get all the incomingMetaList where receivedAt is null
        defaultIncomingMetaShouldNotBeFound("receivedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt is greater than or equal to DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.greaterThanOrEqual=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt is greater than or equal to UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.greaterThanOrEqual=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt is less than or equal to DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.lessThanOrEqual=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt is less than or equal to SMALLER_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.lessThanOrEqual=" + SMALLER_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt is less than DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.lessThan=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt is less than UPDATED_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.lessThan=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReceivedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where receivedAt is greater than DEFAULT_RECEIVED_AT
        defaultIncomingMetaShouldNotBeFound("receivedAt.greaterThan=" + DEFAULT_RECEIVED_AT);

        // Get all the incomingMetaList where receivedAt is greater than SMALLER_RECEIVED_AT
        defaultIncomingMetaShouldBeFound("receivedAt.greaterThan=" + SMALLER_RECEIVED_AT);
    }


    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt equals to DEFAULT_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.equals=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt equals to UPDATED_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.equals=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt not equals to DEFAULT_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.notEquals=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt not equals to UPDATED_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.notEquals=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsInShouldWork() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt in DEFAULT_READ_AT or UPDATED_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.in=" + DEFAULT_READ_AT + "," + UPDATED_READ_AT);

        // Get all the incomingMetaList where readAt equals to UPDATED_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.in=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt is not null
        defaultIncomingMetaShouldBeFound("readAt.specified=true");

        // Get all the incomingMetaList where readAt is null
        defaultIncomingMetaShouldNotBeFound("readAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt is greater than or equal to DEFAULT_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.greaterThanOrEqual=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt is greater than or equal to UPDATED_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.greaterThanOrEqual=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt is less than or equal to DEFAULT_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.lessThanOrEqual=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt is less than or equal to SMALLER_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.lessThanOrEqual=" + SMALLER_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsLessThanSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt is less than DEFAULT_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.lessThan=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt is less than UPDATED_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.lessThan=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void getAllIncomingMetasByReadAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        // Get all the incomingMetaList where readAt is greater than DEFAULT_READ_AT
        defaultIncomingMetaShouldNotBeFound("readAt.greaterThan=" + DEFAULT_READ_AT);

        // Get all the incomingMetaList where readAt is greater than SMALLER_READ_AT
        defaultIncomingMetaShouldBeFound("readAt.greaterThan=" + SMALLER_READ_AT);
    }


    @Test
    @Transactional
    public void getAllIncomingMetasByLetterGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);
        LetterGroup letterGroup = LetterGroupResourceIT.createEntity(em);
        em.persist(letterGroup);
        em.flush();
        incomingMeta.setLetterGroup(letterGroup);
        letterGroup.setIncomingMeta(incomingMeta);
        incomingMetaRepository.saveAndFlush(incomingMeta);
        Long letterGroupId = letterGroup.getId();

        // Get all the incomingMetaList where letterGroup equals to letterGroupId
        defaultIncomingMetaShouldBeFound("letterGroupId.equals=" + letterGroupId);

        // Get all the incomingMetaList where letterGroup equals to letterGroupId + 1
        defaultIncomingMetaShouldNotBeFound("letterGroupId.equals=" + (letterGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIncomingMetaShouldBeFound(String filter) throws Exception {
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomingMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(sameInstant(DEFAULT_RECEIVED_AT))))
            .andExpect(jsonPath("$.[*].readAt").value(hasItem(sameInstant(DEFAULT_READ_AT))));

        // Check, that the count call also returns 1
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIncomingMetaShouldNotBeFound(String filter) throws Exception {
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIncomingMeta() throws Exception {
        // Get the incomingMeta
        restIncomingMetaMockMvc.perform(get("/api/incoming-metas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncomingMeta() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        int databaseSizeBeforeUpdate = incomingMetaRepository.findAll().size();

        // Update the incomingMeta
        IncomingMeta updatedIncomingMeta = incomingMetaRepository.findById(incomingMeta.getId()).get();
        // Disconnect from session so that the updates on updatedIncomingMeta are not directly saved in db
        em.detach(updatedIncomingMeta);
        updatedIncomingMeta
            .receivedAt(UPDATED_RECEIVED_AT)
            .readAt(UPDATED_READ_AT);
        IncomingMetaDTO incomingMetaDTO = incomingMetaMapper.toDto(updatedIncomingMeta);

        restIncomingMetaMockMvc.perform(put("/api/incoming-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomingMetaDTO)))
            .andExpect(status().isOk());

        // Validate the IncomingMeta in the database
        List<IncomingMeta> incomingMetaList = incomingMetaRepository.findAll();
        assertThat(incomingMetaList).hasSize(databaseSizeBeforeUpdate);
        IncomingMeta testIncomingMeta = incomingMetaList.get(incomingMetaList.size() - 1);
        assertThat(testIncomingMeta.getReceivedAt()).isEqualTo(UPDATED_RECEIVED_AT);
        assertThat(testIncomingMeta.getReadAt()).isEqualTo(UPDATED_READ_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingIncomingMeta() throws Exception {
        int databaseSizeBeforeUpdate = incomingMetaRepository.findAll().size();

        // Create the IncomingMeta
        IncomingMetaDTO incomingMetaDTO = incomingMetaMapper.toDto(incomingMeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomingMetaMockMvc.perform(put("/api/incoming-metas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomingMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IncomingMeta in the database
        List<IncomingMeta> incomingMetaList = incomingMetaRepository.findAll();
        assertThat(incomingMetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIncomingMeta() throws Exception {
        // Initialize the database
        incomingMetaRepository.saveAndFlush(incomingMeta);

        int databaseSizeBeforeDelete = incomingMetaRepository.findAll().size();

        // Delete the incomingMeta
        restIncomingMetaMockMvc.perform(delete("/api/incoming-metas/{id}", incomingMeta.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncomingMeta> incomingMetaList = incomingMetaRepository.findAll();
        assertThat(incomingMetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
