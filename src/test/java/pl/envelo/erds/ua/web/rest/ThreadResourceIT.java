package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Thread;
import pl.envelo.erds.ua.repository.ThreadRepository;
import pl.envelo.erds.ua.service.ThreadService;
import pl.envelo.erds.ua.service.dto.ThreadDTO;
import pl.envelo.erds.ua.service.mapper.ThreadMapper;

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
 * Integration tests for the {@link ThreadResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class ThreadResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_LETTER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_LETTER_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ThreadMapper threadMapper;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThreadMockMvc;

    private Thread thread;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thread createEntity(EntityManager em) {
        Thread thread = new Thread()
            .subject(DEFAULT_SUBJECT)
            .lastLetterTime(DEFAULT_LAST_LETTER_TIME);
        return thread;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thread createUpdatedEntity(EntityManager em) {
        Thread thread = new Thread()
            .subject(UPDATED_SUBJECT)
            .lastLetterTime(UPDATED_LAST_LETTER_TIME);
        return thread;
    }

    @BeforeEach
    public void initTest() {
        thread = createEntity(em);
    }

    @Test
    @Transactional
    public void createThread() throws Exception {
        int databaseSizeBeforeCreate = threadRepository.findAll().size();

        // Create the Thread
        ThreadDTO threadDTO = threadMapper.toDto(thread);
        restThreadMockMvc.perform(post("/api/threads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(threadDTO)))
            .andExpect(status().isCreated());

        // Validate the Thread in the database
        List<Thread> threadList = threadRepository.findAll();
        assertThat(threadList).hasSize(databaseSizeBeforeCreate + 1);
        Thread testThread = threadList.get(threadList.size() - 1);
        assertThat(testThread.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testThread.getLastLetterTime()).isEqualTo(DEFAULT_LAST_LETTER_TIME);
    }

    @Test
    @Transactional
    public void createThreadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = threadRepository.findAll().size();

        // Create the Thread with an existing ID
        thread.setId(1L);
        ThreadDTO threadDTO = threadMapper.toDto(thread);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThreadMockMvc.perform(post("/api/threads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(threadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thread in the database
        List<Thread> threadList = threadRepository.findAll();
        assertThat(threadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllThreads() throws Exception {
        // Initialize the database
        threadRepository.saveAndFlush(thread);

        // Get all the threadList
        restThreadMockMvc.perform(get("/api/threads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thread.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].lastLetterTime").value(hasItem(sameInstant(DEFAULT_LAST_LETTER_TIME))));
    }
    
    @Test
    @Transactional
    public void getThread() throws Exception {
        // Initialize the database
        threadRepository.saveAndFlush(thread);

        // Get the thread
        restThreadMockMvc.perform(get("/api/threads/{id}", thread.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thread.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.lastLetterTime").value(sameInstant(DEFAULT_LAST_LETTER_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingThread() throws Exception {
        // Get the thread
        restThreadMockMvc.perform(get("/api/threads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThread() throws Exception {
        // Initialize the database
        threadRepository.saveAndFlush(thread);

        int databaseSizeBeforeUpdate = threadRepository.findAll().size();

        // Update the thread
        Thread updatedThread = threadRepository.findById(thread.getId()).get();
        // Disconnect from session so that the updates on updatedThread are not directly saved in db
        em.detach(updatedThread);
        updatedThread
            .subject(UPDATED_SUBJECT)
            .lastLetterTime(UPDATED_LAST_LETTER_TIME);
        ThreadDTO threadDTO = threadMapper.toDto(updatedThread);

        restThreadMockMvc.perform(put("/api/threads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(threadDTO)))
            .andExpect(status().isOk());

        // Validate the Thread in the database
        List<Thread> threadList = threadRepository.findAll();
        assertThat(threadList).hasSize(databaseSizeBeforeUpdate);
        Thread testThread = threadList.get(threadList.size() - 1);
        assertThat(testThread.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testThread.getLastLetterTime()).isEqualTo(UPDATED_LAST_LETTER_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingThread() throws Exception {
        int databaseSizeBeforeUpdate = threadRepository.findAll().size();

        // Create the Thread
        ThreadDTO threadDTO = threadMapper.toDto(thread);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThreadMockMvc.perform(put("/api/threads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(threadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thread in the database
        List<Thread> threadList = threadRepository.findAll();
        assertThat(threadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteThread() throws Exception {
        // Initialize the database
        threadRepository.saveAndFlush(thread);

        int databaseSizeBeforeDelete = threadRepository.findAll().size();

        // Delete the thread
        restThreadMockMvc.perform(delete("/api/threads/{id}", thread.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Thread> threadList = threadRepository.findAll();
        assertThat(threadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
