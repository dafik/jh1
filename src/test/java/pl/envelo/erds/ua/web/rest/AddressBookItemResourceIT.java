package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.AddressBookItem;
import pl.envelo.erds.ua.domain.UserAccount;
import pl.envelo.erds.ua.repository.AddressBookItemRepository;
import pl.envelo.erds.ua.service.AddressBookItemService;
import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;
import pl.envelo.erds.ua.service.mapper.AddressBookItemMapper;
import pl.envelo.erds.ua.service.dto.AddressBookItemCriteria;
import pl.envelo.erds.ua.service.AddressBookItemQueryService;

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
 * Integration tests for the {@link AddressBookItemResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class AddressBookItemResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_SCHEMA = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private AddressBookItemRepository addressBookItemRepository;

    @Autowired
    private AddressBookItemMapper addressBookItemMapper;

    @Autowired
    private AddressBookItemService addressBookItemService;

    @Autowired
    private AddressBookItemQueryService addressBookItemQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressBookItemMockMvc;

    private AddressBookItem addressBookItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressBookItem createEntity(EntityManager em) {
        AddressBookItem addressBookItem = new AddressBookItem()
            .uid(DEFAULT_UID)
            .schema(DEFAULT_SCHEMA)
            .label(DEFAULT_LABEL);
        return addressBookItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressBookItem createUpdatedEntity(EntityManager em) {
        AddressBookItem addressBookItem = new AddressBookItem()
            .uid(UPDATED_UID)
            .schema(UPDATED_SCHEMA)
            .label(UPDATED_LABEL);
        return addressBookItem;
    }

    @BeforeEach
    public void initTest() {
        addressBookItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddressBookItem() throws Exception {
        int databaseSizeBeforeCreate = addressBookItemRepository.findAll().size();

        // Create the AddressBookItem
        AddressBookItemDTO addressBookItemDTO = addressBookItemMapper.toDto(addressBookItem);
        restAddressBookItemMockMvc.perform(post("/api/address-book-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressBookItemDTO)))
            .andExpect(status().isCreated());

        // Validate the AddressBookItem in the database
        List<AddressBookItem> addressBookItemList = addressBookItemRepository.findAll();
        assertThat(addressBookItemList).hasSize(databaseSizeBeforeCreate + 1);
        AddressBookItem testAddressBookItem = addressBookItemList.get(addressBookItemList.size() - 1);
        assertThat(testAddressBookItem.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAddressBookItem.getSchema()).isEqualTo(DEFAULT_SCHEMA);
        assertThat(testAddressBookItem.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createAddressBookItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressBookItemRepository.findAll().size();

        // Create the AddressBookItem with an existing ID
        addressBookItem.setId(1L);
        AddressBookItemDTO addressBookItemDTO = addressBookItemMapper.toDto(addressBookItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressBookItemMockMvc.perform(post("/api/address-book-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressBookItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AddressBookItem in the database
        List<AddressBookItem> addressBookItemList = addressBookItemRepository.findAll();
        assertThat(addressBookItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAddressBookItems() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList
        restAddressBookItemMockMvc.perform(get("/api/address-book-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressBookItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getAddressBookItem() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get the addressBookItem
        restAddressBookItemMockMvc.perform(get("/api/address-book-items/{id}", addressBookItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addressBookItem.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.schema").value(DEFAULT_SCHEMA))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getAddressBookItemsByIdFiltering() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        Long id = addressBookItem.getId();

        defaultAddressBookItemShouldBeFound("id.equals=" + id);
        defaultAddressBookItemShouldNotBeFound("id.notEquals=" + id);

        defaultAddressBookItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressBookItemShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressBookItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressBookItemShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAddressBookItemsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid equals to DEFAULT_UID
        defaultAddressBookItemShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the addressBookItemList where uid equals to UPDATED_UID
        defaultAddressBookItemShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByUidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid not equals to DEFAULT_UID
        defaultAddressBookItemShouldNotBeFound("uid.notEquals=" + DEFAULT_UID);

        // Get all the addressBookItemList where uid not equals to UPDATED_UID
        defaultAddressBookItemShouldBeFound("uid.notEquals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid in DEFAULT_UID or UPDATED_UID
        defaultAddressBookItemShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the addressBookItemList where uid equals to UPDATED_UID
        defaultAddressBookItemShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid is not null
        defaultAddressBookItemShouldBeFound("uid.specified=true");

        // Get all the addressBookItemList where uid is null
        defaultAddressBookItemShouldNotBeFound("uid.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressBookItemsByUidContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid contains DEFAULT_UID
        defaultAddressBookItemShouldBeFound("uid.contains=" + DEFAULT_UID);

        // Get all the addressBookItemList where uid contains UPDATED_UID
        defaultAddressBookItemShouldNotBeFound("uid.contains=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByUidNotContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where uid does not contain DEFAULT_UID
        defaultAddressBookItemShouldNotBeFound("uid.doesNotContain=" + DEFAULT_UID);

        // Get all the addressBookItemList where uid does not contain UPDATED_UID
        defaultAddressBookItemShouldBeFound("uid.doesNotContain=" + UPDATED_UID);
    }


    @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaIsEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema equals to DEFAULT_SCHEMA
        defaultAddressBookItemShouldBeFound("schema.equals=" + DEFAULT_SCHEMA);

        // Get all the addressBookItemList where schema equals to UPDATED_SCHEMA
        defaultAddressBookItemShouldNotBeFound("schema.equals=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema not equals to DEFAULT_SCHEMA
        defaultAddressBookItemShouldNotBeFound("schema.notEquals=" + DEFAULT_SCHEMA);

        // Get all the addressBookItemList where schema not equals to UPDATED_SCHEMA
        defaultAddressBookItemShouldBeFound("schema.notEquals=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaIsInShouldWork() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema in DEFAULT_SCHEMA or UPDATED_SCHEMA
        defaultAddressBookItemShouldBeFound("schema.in=" + DEFAULT_SCHEMA + "," + UPDATED_SCHEMA);

        // Get all the addressBookItemList where schema equals to UPDATED_SCHEMA
        defaultAddressBookItemShouldNotBeFound("schema.in=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema is not null
        defaultAddressBookItemShouldBeFound("schema.specified=true");

        // Get all the addressBookItemList where schema is null
        defaultAddressBookItemShouldNotBeFound("schema.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema contains DEFAULT_SCHEMA
        defaultAddressBookItemShouldBeFound("schema.contains=" + DEFAULT_SCHEMA);

        // Get all the addressBookItemList where schema contains UPDATED_SCHEMA
        defaultAddressBookItemShouldNotBeFound("schema.contains=" + UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsBySchemaNotContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where schema does not contain DEFAULT_SCHEMA
        defaultAddressBookItemShouldNotBeFound("schema.doesNotContain=" + DEFAULT_SCHEMA);

        // Get all the addressBookItemList where schema does not contain UPDATED_SCHEMA
        defaultAddressBookItemShouldBeFound("schema.doesNotContain=" + UPDATED_SCHEMA);
    }


    @Test
    @Transactional
    public void getAllAddressBookItemsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label equals to DEFAULT_LABEL
        defaultAddressBookItemShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the addressBookItemList where label equals to UPDATED_LABEL
        defaultAddressBookItemShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label not equals to DEFAULT_LABEL
        defaultAddressBookItemShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the addressBookItemList where label not equals to UPDATED_LABEL
        defaultAddressBookItemShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultAddressBookItemShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the addressBookItemList where label equals to UPDATED_LABEL
        defaultAddressBookItemShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label is not null
        defaultAddressBookItemShouldBeFound("label.specified=true");

        // Get all the addressBookItemList where label is null
        defaultAddressBookItemShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressBookItemsByLabelContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label contains DEFAULT_LABEL
        defaultAddressBookItemShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the addressBookItemList where label contains UPDATED_LABEL
        defaultAddressBookItemShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAddressBookItemsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        // Get all the addressBookItemList where label does not contain DEFAULT_LABEL
        defaultAddressBookItemShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the addressBookItemList where label does not contain UPDATED_LABEL
        defaultAddressBookItemShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllAddressBookItemsByUserAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);
        UserAccount userAccount = UserAccountResourceIT.createEntity(em);
        em.persist(userAccount);
        em.flush();
        addressBookItem.setUserAccount(userAccount);
        addressBookItemRepository.saveAndFlush(addressBookItem);
        Long userAccountId = userAccount.getId();

        // Get all the addressBookItemList where userAccount equals to userAccountId
        defaultAddressBookItemShouldBeFound("userAccountId.equals=" + userAccountId);

        // Get all the addressBookItemList where userAccount equals to userAccountId + 1
        defaultAddressBookItemShouldNotBeFound("userAccountId.equals=" + (userAccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressBookItemShouldBeFound(String filter) throws Exception {
        restAddressBookItemMockMvc.perform(get("/api/address-book-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressBookItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restAddressBookItemMockMvc.perform(get("/api/address-book-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressBookItemShouldNotBeFound(String filter) throws Exception {
        restAddressBookItemMockMvc.perform(get("/api/address-book-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressBookItemMockMvc.perform(get("/api/address-book-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAddressBookItem() throws Exception {
        // Get the addressBookItem
        restAddressBookItemMockMvc.perform(get("/api/address-book-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddressBookItem() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        int databaseSizeBeforeUpdate = addressBookItemRepository.findAll().size();

        // Update the addressBookItem
        AddressBookItem updatedAddressBookItem = addressBookItemRepository.findById(addressBookItem.getId()).get();
        // Disconnect from session so that the updates on updatedAddressBookItem are not directly saved in db
        em.detach(updatedAddressBookItem);
        updatedAddressBookItem
            .uid(UPDATED_UID)
            .schema(UPDATED_SCHEMA)
            .label(UPDATED_LABEL);
        AddressBookItemDTO addressBookItemDTO = addressBookItemMapper.toDto(updatedAddressBookItem);

        restAddressBookItemMockMvc.perform(put("/api/address-book-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressBookItemDTO)))
            .andExpect(status().isOk());

        // Validate the AddressBookItem in the database
        List<AddressBookItem> addressBookItemList = addressBookItemRepository.findAll();
        assertThat(addressBookItemList).hasSize(databaseSizeBeforeUpdate);
        AddressBookItem testAddressBookItem = addressBookItemList.get(addressBookItemList.size() - 1);
        assertThat(testAddressBookItem.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAddressBookItem.getSchema()).isEqualTo(UPDATED_SCHEMA);
        assertThat(testAddressBookItem.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingAddressBookItem() throws Exception {
        int databaseSizeBeforeUpdate = addressBookItemRepository.findAll().size();

        // Create the AddressBookItem
        AddressBookItemDTO addressBookItemDTO = addressBookItemMapper.toDto(addressBookItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressBookItemMockMvc.perform(put("/api/address-book-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressBookItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AddressBookItem in the database
        List<AddressBookItem> addressBookItemList = addressBookItemRepository.findAll();
        assertThat(addressBookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAddressBookItem() throws Exception {
        // Initialize the database
        addressBookItemRepository.saveAndFlush(addressBookItem);

        int databaseSizeBeforeDelete = addressBookItemRepository.findAll().size();

        // Delete the addressBookItem
        restAddressBookItemMockMvc.perform(delete("/api/address-book-items/{id}", addressBookItem.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AddressBookItem> addressBookItemList = addressBookItemRepository.findAll();
        assertThat(addressBookItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
