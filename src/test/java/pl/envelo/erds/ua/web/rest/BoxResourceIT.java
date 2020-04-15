package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Box;
import pl.envelo.erds.ua.repository.BoxRepository;
import pl.envelo.erds.ua.service.BoxService;
import pl.envelo.erds.ua.service.dto.BoxDTO;
import pl.envelo.erds.ua.service.mapper.BoxMapper;

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

import pl.envelo.erds.ua.domain.enumeration.BoxType;
/**
 * Integration tests for the {@link BoxResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class BoxResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_NAMESPACE = "AAAAAAAAAA";
    private static final String UPDATED_NAMESPACE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final BoxType DEFAULT_BOX_TYPE = BoxType.NATURAL;
    private static final BoxType UPDATED_BOX_TYPE = BoxType.LEGAL;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_RULES_ACCEPTANCE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RULES_ACCEPTANCE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private BoxMapper boxMapper;

    @Autowired
    private BoxService boxService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoxMockMvc;

    private Box box;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Box createEntity(EntityManager em) {
        Box box = new Box()
            .uid(DEFAULT_UID)
            .namespace(DEFAULT_NAMESPACE)
            .label(DEFAULT_LABEL)
            .boxType(DEFAULT_BOX_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .rulesAcceptanceDate(DEFAULT_RULES_ACCEPTANCE_DATE);
        return box;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Box createUpdatedEntity(EntityManager em) {
        Box box = new Box()
            .uid(UPDATED_UID)
            .namespace(UPDATED_NAMESPACE)
            .label(UPDATED_LABEL)
            .boxType(UPDATED_BOX_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .rulesAcceptanceDate(UPDATED_RULES_ACCEPTANCE_DATE);
        return box;
    }

    @BeforeEach
    public void initTest() {
        box = createEntity(em);
    }

    @Test
    @Transactional
    public void createBox() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box
        BoxDTO boxDTO = boxMapper.toDto(box);
        restBoxMockMvc.perform(post("/api/boxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate + 1);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testBox.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testBox.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testBox.getBoxType()).isEqualTo(DEFAULT_BOX_TYPE);
        assertThat(testBox.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testBox.getRulesAcceptanceDate()).isEqualTo(DEFAULT_RULES_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    public void createBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box with an existing ID
        box.setId(1L);
        BoxDTO boxDTO = boxMapper.toDto(box);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxMockMvc.perform(post("/api/boxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBoxes() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get all the boxList
        restBoxMockMvc.perform(get("/api/boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(box.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].boxType").value(hasItem(DEFAULT_BOX_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].rulesAcceptanceDate").value(hasItem(sameInstant(DEFAULT_RULES_ACCEPTANCE_DATE))));
    }
    
    @Test
    @Transactional
    public void getBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", box.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(box.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.boxType").value(DEFAULT_BOX_TYPE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.rulesAcceptanceDate").value(sameInstant(DEFAULT_RULES_ACCEPTANCE_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingBox() throws Exception {
        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Update the box
        Box updatedBox = boxRepository.findById(box.getId()).get();
        // Disconnect from session so that the updates on updatedBox are not directly saved in db
        em.detach(updatedBox);
        updatedBox
            .uid(UPDATED_UID)
            .namespace(UPDATED_NAMESPACE)
            .label(UPDATED_LABEL)
            .boxType(UPDATED_BOX_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .rulesAcceptanceDate(UPDATED_RULES_ACCEPTANCE_DATE);
        BoxDTO boxDTO = boxMapper.toDto(updatedBox);

        restBoxMockMvc.perform(put("/api/boxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isOk());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testBox.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testBox.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testBox.getBoxType()).isEqualTo(UPDATED_BOX_TYPE);
        assertThat(testBox.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testBox.getRulesAcceptanceDate()).isEqualTo(UPDATED_RULES_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBox() throws Exception {
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Create the Box
        BoxDTO boxDTO = boxMapper.toDto(box);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoxMockMvc.perform(put("/api/boxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        int databaseSizeBeforeDelete = boxRepository.findAll().size();

        // Delete the box
        restBoxMockMvc.perform(delete("/api/boxes/{id}", box.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
