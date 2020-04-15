package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Letter;
import pl.envelo.erds.ua.domain.Actor;
import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.repository.LetterRepository;
import pl.envelo.erds.ua.service.LetterService;
import pl.envelo.erds.ua.service.dto.LetterDTO;
import pl.envelo.erds.ua.service.mapper.LetterMapper;
import pl.envelo.erds.ua.service.dto.LetterCriteria;
import pl.envelo.erds.ua.service.LetterQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LetterResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class LetterResourceIT {

    private static final String DEFAULT_EAN = "AAAAAAAAAA";
    private static final String UPDATED_EAN = "BBBBBBBBBB";

    private static final String DEFAULT_ERDS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ERDS_ID = "BBBBBBBBBB";

    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private LetterMapper letterMapper;

    @Autowired
    private LetterService letterService;

    @Autowired
    private LetterQueryService letterQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLetterMockMvc;

    private Letter letter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Letter createEntity(EntityManager em) {
        Letter letter = new Letter()
            .ean(DEFAULT_EAN)
            .erdsId(DEFAULT_ERDS_ID);
        return letter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Letter createUpdatedEntity(EntityManager em) {
        Letter letter = new Letter()
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID);
        return letter;
    }

    @BeforeEach
    public void initTest() {
        letter = createEntity(em);
    }

    @Test
    @Transactional
    public void createLetter() throws Exception {
        int databaseSizeBeforeCreate = letterRepository.findAll().size();

        // Create the Letter
        LetterDTO letterDTO = letterMapper.toDto(letter);
        restLetterMockMvc.perform(post("/api/letters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterDTO)))
            .andExpect(status().isCreated());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeCreate + 1);
        Letter testLetter = letterList.get(letterList.size() - 1);
        assertThat(testLetter.getEan()).isEqualTo(DEFAULT_EAN);
        assertThat(testLetter.getErdsId()).isEqualTo(DEFAULT_ERDS_ID);
    }

    @Test
    @Transactional
    public void createLetterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = letterRepository.findAll().size();

        // Create the Letter with an existing ID
        letter.setId(1L);
        LetterDTO letterDTO = letterMapper.toDto(letter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLetterMockMvc.perform(post("/api/letters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLetters() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList
        restLetterMockMvc.perform(get("/api/letters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(letter.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)));
    }
    
    @Test
    @Transactional
    public void getLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get the letter
        restLetterMockMvc.perform(get("/api/letters/{id}", letter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(letter.getId().intValue()))
            .andExpect(jsonPath("$.ean").value(DEFAULT_EAN))
            .andExpect(jsonPath("$.erdsId").value(DEFAULT_ERDS_ID));
    }


    @Test
    @Transactional
    public void getLettersByIdFiltering() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        Long id = letter.getId();

        defaultLetterShouldBeFound("id.equals=" + id);
        defaultLetterShouldNotBeFound("id.notEquals=" + id);

        defaultLetterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLetterShouldNotBeFound("id.greaterThan=" + id);

        defaultLetterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLetterShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLettersByEanIsEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean equals to DEFAULT_EAN
        defaultLetterShouldBeFound("ean.equals=" + DEFAULT_EAN);

        // Get all the letterList where ean equals to UPDATED_EAN
        defaultLetterShouldNotBeFound("ean.equals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllLettersByEanIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean not equals to DEFAULT_EAN
        defaultLetterShouldNotBeFound("ean.notEquals=" + DEFAULT_EAN);

        // Get all the letterList where ean not equals to UPDATED_EAN
        defaultLetterShouldBeFound("ean.notEquals=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllLettersByEanIsInShouldWork() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean in DEFAULT_EAN or UPDATED_EAN
        defaultLetterShouldBeFound("ean.in=" + DEFAULT_EAN + "," + UPDATED_EAN);

        // Get all the letterList where ean equals to UPDATED_EAN
        defaultLetterShouldNotBeFound("ean.in=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllLettersByEanIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean is not null
        defaultLetterShouldBeFound("ean.specified=true");

        // Get all the letterList where ean is null
        defaultLetterShouldNotBeFound("ean.specified=false");
    }
                @Test
    @Transactional
    public void getAllLettersByEanContainsSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean contains DEFAULT_EAN
        defaultLetterShouldBeFound("ean.contains=" + DEFAULT_EAN);

        // Get all the letterList where ean contains UPDATED_EAN
        defaultLetterShouldNotBeFound("ean.contains=" + UPDATED_EAN);
    }

    @Test
    @Transactional
    public void getAllLettersByEanNotContainsSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where ean does not contain DEFAULT_EAN
        defaultLetterShouldNotBeFound("ean.doesNotContain=" + DEFAULT_EAN);

        // Get all the letterList where ean does not contain UPDATED_EAN
        defaultLetterShouldBeFound("ean.doesNotContain=" + UPDATED_EAN);
    }


    @Test
    @Transactional
    public void getAllLettersByErdsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId equals to DEFAULT_ERDS_ID
        defaultLetterShouldBeFound("erdsId.equals=" + DEFAULT_ERDS_ID);

        // Get all the letterList where erdsId equals to UPDATED_ERDS_ID
        defaultLetterShouldNotBeFound("erdsId.equals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllLettersByErdsIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId not equals to DEFAULT_ERDS_ID
        defaultLetterShouldNotBeFound("erdsId.notEquals=" + DEFAULT_ERDS_ID);

        // Get all the letterList where erdsId not equals to UPDATED_ERDS_ID
        defaultLetterShouldBeFound("erdsId.notEquals=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllLettersByErdsIdIsInShouldWork() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId in DEFAULT_ERDS_ID or UPDATED_ERDS_ID
        defaultLetterShouldBeFound("erdsId.in=" + DEFAULT_ERDS_ID + "," + UPDATED_ERDS_ID);

        // Get all the letterList where erdsId equals to UPDATED_ERDS_ID
        defaultLetterShouldNotBeFound("erdsId.in=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllLettersByErdsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId is not null
        defaultLetterShouldBeFound("erdsId.specified=true");

        // Get all the letterList where erdsId is null
        defaultLetterShouldNotBeFound("erdsId.specified=false");
    }
                @Test
    @Transactional
    public void getAllLettersByErdsIdContainsSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId contains DEFAULT_ERDS_ID
        defaultLetterShouldBeFound("erdsId.contains=" + DEFAULT_ERDS_ID);

        // Get all the letterList where erdsId contains UPDATED_ERDS_ID
        defaultLetterShouldNotBeFound("erdsId.contains=" + UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void getAllLettersByErdsIdNotContainsSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList where erdsId does not contain DEFAULT_ERDS_ID
        defaultLetterShouldNotBeFound("erdsId.doesNotContain=" + DEFAULT_ERDS_ID);

        // Get all the letterList where erdsId does not contain UPDATED_ERDS_ID
        defaultLetterShouldBeFound("erdsId.doesNotContain=" + UPDATED_ERDS_ID);
    }


    @Test
    @Transactional
    public void getAllLettersBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);
        Actor sender = ActorResourceIT.createEntity(em);
        em.persist(sender);
        em.flush();
        letter.setSender(sender);
        letterRepository.saveAndFlush(letter);
        Long senderId = sender.getId();

        // Get all the letterList where sender equals to senderId
        defaultLetterShouldBeFound("senderId.equals=" + senderId);

        // Get all the letterList where sender equals to senderId + 1
        defaultLetterShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllLettersByReceipientIsEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);
        Actor receipient = ActorResourceIT.createEntity(em);
        em.persist(receipient);
        em.flush();
        letter.setReceipient(receipient);
        letterRepository.saveAndFlush(letter);
        Long receipientId = receipient.getId();

        // Get all the letterList where receipient equals to receipientId
        defaultLetterShouldBeFound("receipientId.equals=" + receipientId);

        // Get all the letterList where receipient equals to receipientId + 1
        defaultLetterShouldNotBeFound("receipientId.equals=" + (receipientId + 1));
    }


    @Test
    @Transactional
    public void getAllLettersByLetterGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);
        LetterGroup letterGroup = LetterGroupResourceIT.createEntity(em);
        em.persist(letterGroup);
        em.flush();
        letter.setLetterGroup(letterGroup);
        letterRepository.saveAndFlush(letter);
        Long letterGroupId = letterGroup.getId();

        // Get all the letterList where letterGroup equals to letterGroupId
        defaultLetterShouldBeFound("letterGroupId.equals=" + letterGroupId);

        // Get all the letterList where letterGroup equals to letterGroupId + 1
        defaultLetterShouldNotBeFound("letterGroupId.equals=" + (letterGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLetterShouldBeFound(String filter) throws Exception {
        restLetterMockMvc.perform(get("/api/letters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(letter.getId().intValue())))
            .andExpect(jsonPath("$.[*].ean").value(hasItem(DEFAULT_EAN)))
            .andExpect(jsonPath("$.[*].erdsId").value(hasItem(DEFAULT_ERDS_ID)));

        // Check, that the count call also returns 1
        restLetterMockMvc.perform(get("/api/letters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLetterShouldNotBeFound(String filter) throws Exception {
        restLetterMockMvc.perform(get("/api/letters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLetterMockMvc.perform(get("/api/letters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLetter() throws Exception {
        // Get the letter
        restLetterMockMvc.perform(get("/api/letters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        int databaseSizeBeforeUpdate = letterRepository.findAll().size();

        // Update the letter
        Letter updatedLetter = letterRepository.findById(letter.getId()).get();
        // Disconnect from session so that the updates on updatedLetter are not directly saved in db
        em.detach(updatedLetter);
        updatedLetter
            .ean(UPDATED_EAN)
            .erdsId(UPDATED_ERDS_ID);
        LetterDTO letterDTO = letterMapper.toDto(updatedLetter);

        restLetterMockMvc.perform(put("/api/letters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterDTO)))
            .andExpect(status().isOk());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeUpdate);
        Letter testLetter = letterList.get(letterList.size() - 1);
        assertThat(testLetter.getEan()).isEqualTo(UPDATED_EAN);
        assertThat(testLetter.getErdsId()).isEqualTo(UPDATED_ERDS_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingLetter() throws Exception {
        int databaseSizeBeforeUpdate = letterRepository.findAll().size();

        // Create the Letter
        LetterDTO letterDTO = letterMapper.toDto(letter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLetterMockMvc.perform(put("/api/letters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        int databaseSizeBeforeDelete = letterRepository.findAll().size();

        // Delete the letter
        restLetterMockMvc.perform(delete("/api/letters/{id}", letter.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
