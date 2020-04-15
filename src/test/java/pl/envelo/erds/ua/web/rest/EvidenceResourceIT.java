package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Evidence;
import pl.envelo.erds.ua.domain.Box;
import pl.envelo.erds.ua.repository.EvidenceRepository;
import pl.envelo.erds.ua.service.EvidenceService;
import pl.envelo.erds.ua.service.dto.EvidenceDTO;
import pl.envelo.erds.ua.service.mapper.EvidenceMapper;
import pl.envelo.erds.ua.service.dto.EvidenceCriteria;
import pl.envelo.erds.ua.service.EvidenceQueryService;

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

import pl.envelo.erds.ua.domain.enumeration.EvidenceType;
/**
 * Integration tests for the {@link EvidenceResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class EvidenceResourceIT {

    private static final String DEFAULT_EAN = "AAAAAAAAAA";
    private static final String UPDATED_EAN = "BBBBBBBBBB";

    private static final String DEFAULT_ERDS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ERDS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROCESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final EvidenceType DEFAULT_TYPE = EvidenceType.A1;
    private static final EvidenceType UPDATED_TYPE = EvidenceType.A2;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Autowired
    private EvidenceMapper evidenceMapper;

    @Autowired
    private EvidenceService evidenceService;

    @Autowired
    private EvidenceQueryService evidenceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvidenceMockMvc;

    private Evidence evidence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evidence createEntity(EntityManager em) {
        Evidence evidence = new Evidence()
            .ean(DEFAULT_EAN)
            .erdsId(DEFAULT_ERDS_ID)
            .processId(DEFAULT_PROCESS_ID)
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .location(DEFAULT_LOCATION);
        return evidence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evidence createUpdatedEntity(EntityManager em) {
        Evidence evidence = new Evidence()
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID)
            .processId(UPDATED_PROCESS_ID)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION);
        return evidence;
    }

    @BeforeEach
    public void initTest() {
        evidence = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvidence() throws Exception {
        int databaseSizeBeforeCreate = evidenceRepository.findAll().size();

        // Create the Evidence
        EvidenceDTO evidenceDTO = evidenceMapper.toDto(evidence);
        restEvidenceMockMvc.perform(post("/api/evidences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evidenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Evidence in the database
        List<Evidence> evidenceList = evidenceRepository.findAll();
        assertThat(evidenceList).hasSize(databaseSizeBeforeCreate + 1);
        Evidence testEvidence = evidenceList.get(evidenceList.size() - 1);
        assertThat(testEvidence.getEan()).isEqualTo(DEFAULT_EAN);
        assertThat(testEvidence.getErdsId()).isEqualTo(DEFAULT_ERDS_ID);
        assertThat(testEvidence.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
        assertThat(testEvidence.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEvidence.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEvidence.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createEvidenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evidenceRepository.findAll().size();

        // Create the Evidence with an existing ID
        evidence.setId(1L);
        EvidenceDTO evidenceDTO = evidenceMapper.toDto(evidence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvidenceMockMvc.perform(post("/api/evidences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evidenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evidence in the database
        List<Evidence> evidenceList = evidenceRepository.findAll();
        assertThat(evidenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEvidences() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList
        restEvidenceMockMvc.perform(get("/api/evidences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evidence.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }
    
    @Test
    @Transactional
    public void getEvidence() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get the evidence
        restEvidenceMockMvc.perform(get("/api/evidences/{id}", evidence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evidence.getId().intValue()))
            .andExpect(jsonPath("$.ean").value(DEFAULT_EAN))
            .andExpect(jsonPath("$.erdsId").value(DEFAULT_ERDS_ID))
            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }


    @Test
    @Transactional
    public void getEvidencesByIdFiltering() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        Long id = evidence.getId();

        defaultEvidenceShouldBeFound("id.equals=" + id);
        defaultEvidenceShouldNotBeFound("id.notEquals=" + id);

        defaultEvidenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEvidenceShouldNotBeFound("id.greaterThan=" + id);

        defaultEvidenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEvidenceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEvidencesByEanIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean equals to DEFAULT_EAN
        defaultEvidenceShouldBeFound("ean.equals=" + DEFAULT_EAN);

        // Get all the evidenceList where ean equals to UPDATED_EAN
        defaultEvidenceShouldNotBeFound("ean.equals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllEvidencesByEanIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean not equals to DEFAULT_EAN
        defaultEvidenceShouldNotBeFound("ean.notEquals=" + DEFAULT_EAN);

        // Get all the evidenceList where ean not equals to UPDATED_EAN
        defaultEvidenceShouldBeFound("ean.notEquals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllEvidencesByEanIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean in DEFAULT_EAN or UPDATED_EAN
        defaultEvidenceShouldBeFound("ean.in=" + DEFAULT_EAN + "," + UPDATED_EAN);

        // Get all the evidenceList where ean equals to UPDATED_EAN
        defaultEvidenceShouldNotBeFound("ean.in=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllEvidencesByEanIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean is not null
        defaultEvidenceShouldBeFound("ean.specified=true");

        // Get all the evidenceList where ean is null
        defaultEvidenceShouldNotBeFound("ean.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvidencesByEanContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean contains DEFAULT_EAN
        defaultEvidenceShouldBeFound("ean.contains=" + DEFAULT_EAN);

        // Get all the evidenceList where ean contains UPDATED_EAN
        defaultEvidenceShouldNotBeFound("ean.contains=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllEvidencesByEanNotContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where ean does not contain DEFAULT_EAN
        defaultEvidenceShouldNotBeFound("ean.doesNotContain=" + DEFAULT_EAN);

        // Get all the evidenceList where ean does not contain UPDATED_EAN
        defaultEvidenceShouldBeFound("ean.doesNotContain=" + UPDATED_EAN);
    }


    @Test
    @Transactional
    public void getAllEvidencesByErdsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId equals to DEFAULT_ERDS_ID
        defaultEvidenceShouldBeFound("erdsId.equals=" + DEFAULT_ERDS_ID);

        // Get all the evidenceList where erdsId equals to UPDATED_ERDS_ID
        defaultEvidenceShouldNotBeFound("erdsId.equals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByErdsIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId not equals to DEFAULT_ERDS_ID
        defaultEvidenceShouldNotBeFound("erdsId.notEquals=" + DEFAULT_ERDS_ID);

        // Get all the evidenceList where erdsId not equals to UPDATED_ERDS_ID
        defaultEvidenceShouldBeFound("erdsId.notEquals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByErdsIdIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId in DEFAULT_ERDS_ID or UPDATED_ERDS_ID
        defaultEvidenceShouldBeFound("erdsId.in=" + DEFAULT_ERDS_ID + "," + UPDATED_ERDS_ID);

        // Get all the evidenceList where erdsId equals to UPDATED_ERDS_ID
        defaultEvidenceShouldNotBeFound("erdsId.in=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByErdsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId is not null
        defaultEvidenceShouldBeFound("erdsId.specified=true");

        // Get all the evidenceList where erdsId is null
        defaultEvidenceShouldNotBeFound("erdsId.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvidencesByErdsIdContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId contains DEFAULT_ERDS_ID
        defaultEvidenceShouldBeFound("erdsId.contains=" + DEFAULT_ERDS_ID);

        // Get all the evidenceList where erdsId contains UPDATED_ERDS_ID
        defaultEvidenceShouldNotBeFound("erdsId.contains=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByErdsIdNotContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where erdsId does not contain DEFAULT_ERDS_ID
        defaultEvidenceShouldNotBeFound("erdsId.doesNotContain=" + DEFAULT_ERDS_ID);

        // Get all the evidenceList where erdsId does not contain UPDATED_ERDS_ID
        defaultEvidenceShouldBeFound("erdsId.doesNotContain=" + UPDATED_ERDS_ID);
    }


    @Test
    @Transactional
    public void getAllEvidencesByProcessIdIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId equals to DEFAULT_PROCESS_ID
        defaultEvidenceShouldBeFound("processId.equals=" + DEFAULT_PROCESS_ID);

        // Get all the evidenceList where processId equals to UPDATED_PROCESS_ID
        defaultEvidenceShouldNotBeFound("processId.equals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByProcessIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId not equals to DEFAULT_PROCESS_ID
        defaultEvidenceShouldNotBeFound("processId.notEquals=" + DEFAULT_PROCESS_ID);

        // Get all the evidenceList where processId not equals to UPDATED_PROCESS_ID
        defaultEvidenceShouldBeFound("processId.notEquals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByProcessIdIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId in DEFAULT_PROCESS_ID or UPDATED_PROCESS_ID
        defaultEvidenceShouldBeFound("processId.in=" + DEFAULT_PROCESS_ID + "," + UPDATED_PROCESS_ID);

        // Get all the evidenceList where processId equals to UPDATED_PROCESS_ID
        defaultEvidenceShouldNotBeFound("processId.in=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByProcessIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId is not null
        defaultEvidenceShouldBeFound("processId.specified=true");

        // Get all the evidenceList where processId is null
        defaultEvidenceShouldNotBeFound("processId.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvidencesByProcessIdContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId contains DEFAULT_PROCESS_ID
        defaultEvidenceShouldBeFound("processId.contains=" + DEFAULT_PROCESS_ID);

        // Get all the evidenceList where processId contains UPDATED_PROCESS_ID
        defaultEvidenceShouldNotBeFound("processId.contains=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllEvidencesByProcessIdNotContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where processId does not contain DEFAULT_PROCESS_ID
        defaultEvidenceShouldNotBeFound("processId.doesNotContain=" + DEFAULT_PROCESS_ID);

        // Get all the evidenceList where processId does not contain UPDATED_PROCESS_ID
        defaultEvidenceShouldBeFound("processId.doesNotContain=" + UPDATED_PROCESS_ID);
    }


    @Test
    @Transactional
    public void getAllEvidencesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date equals to DEFAULT_DATE
        defaultEvidenceShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the evidenceList where date equals to UPDATED_DATE
        defaultEvidenceShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date not equals to DEFAULT_DATE
        defaultEvidenceShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the evidenceList where date not equals to UPDATED_DATE
        defaultEvidenceShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date in DEFAULT_DATE or UPDATED_DATE
        defaultEvidenceShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the evidenceList where date equals to UPDATED_DATE
        defaultEvidenceShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date is not null
        defaultEvidenceShouldBeFound("date.specified=true");

        // Get all the evidenceList where date is null
        defaultEvidenceShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date is greater than or equal to DEFAULT_DATE
        defaultEvidenceShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the evidenceList where date is greater than or equal to UPDATED_DATE
        defaultEvidenceShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date is less than or equal to DEFAULT_DATE
        defaultEvidenceShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the evidenceList where date is less than or equal to SMALLER_DATE
        defaultEvidenceShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date is less than DEFAULT_DATE
        defaultEvidenceShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the evidenceList where date is less than UPDATED_DATE
        defaultEvidenceShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where date is greater than DEFAULT_DATE
        defaultEvidenceShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the evidenceList where date is greater than SMALLER_DATE
        defaultEvidenceShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllEvidencesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where type equals to DEFAULT_TYPE
        defaultEvidenceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the evidenceList where type equals to UPDATED_TYPE
        defaultEvidenceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where type not equals to DEFAULT_TYPE
        defaultEvidenceShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the evidenceList where type not equals to UPDATED_TYPE
        defaultEvidenceShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultEvidenceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the evidenceList where type equals to UPDATED_TYPE
        defaultEvidenceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEvidencesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where type is not null
        defaultEvidenceShouldBeFound("type.specified=true");

        // Get all the evidenceList where type is null
        defaultEvidenceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllEvidencesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location equals to DEFAULT_LOCATION
        defaultEvidenceShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the evidenceList where location equals to UPDATED_LOCATION
        defaultEvidenceShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEvidencesByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location not equals to DEFAULT_LOCATION
        defaultEvidenceShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the evidenceList where location not equals to UPDATED_LOCATION
        defaultEvidenceShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEvidencesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultEvidenceShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the evidenceList where location equals to UPDATED_LOCATION
        defaultEvidenceShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEvidencesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location is not null
        defaultEvidenceShouldBeFound("location.specified=true");

        // Get all the evidenceList where location is null
        defaultEvidenceShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvidencesByLocationContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location contains DEFAULT_LOCATION
        defaultEvidenceShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the evidenceList where location contains UPDATED_LOCATION
        defaultEvidenceShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEvidencesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        // Get all the evidenceList where location does not contain DEFAULT_LOCATION
        defaultEvidenceShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the evidenceList where location does not contain UPDATED_LOCATION
        defaultEvidenceShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllEvidencesByBoxIsEqualToSomething() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);
        Box box = BoxResourceIT.createEntity(em);
        em.persist(box);
        em.flush();
        evidence.setBox(box);
        evidenceRepository.saveAndFlush(evidence);
        Long boxId = box.getId();

        // Get all the evidenceList where box equals to boxId
        defaultEvidenceShouldBeFound("boxId.equals=" + boxId);

        // Get all the evidenceList where box equals to boxId + 1
        defaultEvidenceShouldNotBeFound("boxId.equals=" + (boxId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvidenceShouldBeFound(String filter) throws Exception {
        restEvidenceMockMvc.perform(get("/api/evidences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evidence.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restEvidenceMockMvc.perform(get("/api/evidences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvidenceShouldNotBeFound(String filter) throws Exception {
        restEvidenceMockMvc.perform(get("/api/evidences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvidenceMockMvc.perform(get("/api/evidences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEvidence() throws Exception {
        // Get the evidence
        restEvidenceMockMvc.perform(get("/api/evidences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvidence() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        int databaseSizeBeforeUpdate = evidenceRepository.findAll().size();

        // Update the evidence
        Evidence updatedEvidence = evidenceRepository.findById(evidence.getId()).get();
        // Disconnect from session so that the updates on updatedEvidence are not directly saved in db
        em.detach(updatedEvidence);
        updatedEvidence
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID)
            .processId(UPDATED_PROCESS_ID)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION);
        EvidenceDTO evidenceDTO = evidenceMapper.toDto(updatedEvidence);

        restEvidenceMockMvc.perform(put("/api/evidences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evidenceDTO)))
            .andExpect(status().isOk());

        // Validate the Evidence in the database
        List<Evidence> evidenceList = evidenceRepository.findAll();
        assertThat(evidenceList).hasSize(databaseSizeBeforeUpdate);
        Evidence testEvidence = evidenceList.get(evidenceList.size() - 1);
        assertThat(testEvidence.getEan()).isEqualTo(UPDATED_EAN);
        assertThat(testEvidence.getErdsId()).isEqualTo(UPDATED_ERDS_ID);
        assertThat(testEvidence.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testEvidence.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEvidence.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEvidence.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingEvidence() throws Exception {
        int databaseSizeBeforeUpdate = evidenceRepository.findAll().size();

        // Create the Evidence
        EvidenceDTO evidenceDTO = evidenceMapper.toDto(evidence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvidenceMockMvc.perform(put("/api/evidences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evidenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evidence in the database
        List<Evidence> evidenceList = evidenceRepository.findAll();
        assertThat(evidenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvidence() throws Exception {
        // Initialize the database
        evidenceRepository.saveAndFlush(evidence);

        int databaseSizeBeforeDelete = evidenceRepository.findAll().size();

        // Delete the evidence
        restEvidenceMockMvc.perform(delete("/api/evidences/{id}", evidence.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evidence> evidenceList = evidenceRepository.findAll();
        assertThat(evidenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
