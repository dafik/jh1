package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.Actor;
import pl.envelo.erds.ua.repository.ActorRepository;
import pl.envelo.erds.ua.service.ActorService;
import pl.envelo.erds.ua.service.dto.ActorDTO;
import pl.envelo.erds.ua.service.mapper.ActorMapper;
import pl.envelo.erds.ua.service.dto.ActorCriteria;
import pl.envelo.erds.ua.service.ActorQueryService;

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
 * Integration tests for the {@link ActorResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class ActorResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_SCHEMA = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ActorQueryService actorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActorMockMvc;

    private Actor actor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
            .uid(DEFAULT_UID)
            .schema(DEFAULT_SCHEMA)
            .label(DEFAULT_LABEL);
        return actor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createUpdatedEntity(EntityManager em) {
        Actor actor = new Actor()
            .uid(UPDATED_UID)
            .schema(UPDATED_SCHEMA)
            .label(UPDATED_LABEL);
        return actor;
    }

    @BeforeEach
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);
        restActorMockMvc.perform(post("/api/actors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testActor.getSchema()).isEqualTo(DEFAULT_SCHEMA);
        assertThat(testActor.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor with an existing ID
        actor.setId(1L);
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc.perform(post("/api/actors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.schema").value(DEFAULT_SCHEMA))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getActorsByIdFiltering() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        Long id = actor.getId();

        defaultActorShouldBeFound("id.equals=" + id);
        defaultActorShouldNotBeFound("id.notEquals=" + id);

        defaultActorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActorShouldNotBeFound("id.greaterThan=" + id);

        defaultActorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllActorsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid equals to DEFAULT_UID
        defaultActorShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the actorList where uid equals to UPDATED_UID
        defaultActorShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllActorsByUidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid not equals to DEFAULT_UID
        defaultActorShouldNotBeFound("uid.notEquals=" + DEFAULT_UID);

        // Get all the actorList where uid not equals to UPDATED_UID
        defaultActorShouldBeFound("uid.notEquals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllActorsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid in DEFAULT_UID or UPDATED_UID
        defaultActorShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the actorList where uid equals to UPDATED_UID
        defaultActorShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllActorsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid is not null
        defaultActorShouldBeFound("uid.specified=true");

        // Get all the actorList where uid is null
        defaultActorShouldNotBeFound("uid.specified=false");
    }
                @Test
    @Transactional
    public void getAllActorsByUidContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid contains DEFAULT_UID
        defaultActorShouldBeFound("uid.contains=" + DEFAULT_UID);

        // Get all the actorList where uid contains UPDATED_UID
        defaultActorShouldNotBeFound("uid.contains=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllActorsByUidNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where uid does not contain DEFAULT_UID
        defaultActorShouldNotBeFound("uid.doesNotContain=" + DEFAULT_UID);

        // Get all the actorList where uid does not contain UPDATED_UID
        defaultActorShouldBeFound("uid.doesNotContain=" + UPDATED_UID);
    }


    @Test
    @Transactional
    public void getAllActorsBySchemaIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema equals to DEFAULT_SCHEMA
        defaultActorShouldBeFound("schema.equals=" + DEFAULT_SCHEMA);

        // Get all the actorList where schema equals to UPDATED_SCHEMA
        defaultActorShouldNotBeFound("schema.equals=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllActorsBySchemaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema not equals to DEFAULT_SCHEMA
        defaultActorShouldNotBeFound("schema.notEquals=" + DEFAULT_SCHEMA);

        // Get all the actorList where schema not equals to UPDATED_SCHEMA
        defaultActorShouldBeFound("schema.notEquals=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllActorsBySchemaIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema in DEFAULT_SCHEMA or UPDATED_SCHEMA
        defaultActorShouldBeFound("schema.in=" + DEFAULT_SCHEMA + "," + UPDATED_SCHEMA);

        // Get all the actorList where schema equals to UPDATED_SCHEMA
        defaultActorShouldNotBeFound("schema.in=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllActorsBySchemaIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema is not null
        defaultActorShouldBeFound("schema.specified=true");

        // Get all the actorList where schema is null
        defaultActorShouldNotBeFound("schema.specified=false");
    }
                @Test
    @Transactional
    public void getAllActorsBySchemaContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema contains DEFAULT_SCHEMA
        defaultActorShouldBeFound("schema.contains=" + DEFAULT_SCHEMA);

        // Get all the actorList where schema contains UPDATED_SCHEMA
        defaultActorShouldNotBeFound("schema.contains=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllActorsBySchemaNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where schema does not contain DEFAULT_SCHEMA
        defaultActorShouldNotBeFound("schema.doesNotContain=" + DEFAULT_SCHEMA);

        // Get all the actorList where schema does not contain UPDATED_SCHEMA
        defaultActorShouldBeFound("schema.doesNotContain=" + UPDATED_SCHEMA);
    }


    @Test
    @Transactional
    public void getAllActorsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label equals to DEFAULT_LABEL
        defaultActorShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the actorList where label equals to UPDATED_LABEL
        defaultActorShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllActorsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label not equals to DEFAULT_LABEL
        defaultActorShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the actorList where label not equals to UPDATED_LABEL
        defaultActorShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllActorsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultActorShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the actorList where label equals to UPDATED_LABEL
        defaultActorShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllActorsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label is not null
        defaultActorShouldBeFound("label.specified=true");

        // Get all the actorList where label is null
        defaultActorShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllActorsByLabelContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label contains DEFAULT_LABEL
        defaultActorShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the actorList where label contains UPDATED_LABEL
        defaultActorShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllActorsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where label does not contain DEFAULT_LABEL
        defaultActorShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the actorList where label does not contain UPDATED_LABEL
        defaultActorShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActorShouldBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restActorMockMvc.perform(get("/api/actors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActorShouldNotBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActorMockMvc.perform(get("/api/actors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findById(actor.getId()).get();
        // Disconnect from session so that the updates on updatedActor are not directly saved in db
        em.detach(updatedActor);
        updatedActor
            .uid(UPDATED_UID)
            .schema(UPDATED_SCHEMA)
            .label(UPDATED_LABEL);
        ActorDTO actorDTO = actorMapper.toDto(updatedActor);

        restActorMockMvc.perform(put("/api/actors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testActor.getSchema()).isEqualTo(UPDATED_SCHEMA);
        assertThat(testActor.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorMockMvc.perform(put("/api/actors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Delete the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
