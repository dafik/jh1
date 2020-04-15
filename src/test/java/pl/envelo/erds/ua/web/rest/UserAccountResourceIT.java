package pl.envelo.erds.ua.web.rest;

import pl.envelo.erds.ua.UseragentApp;
import pl.envelo.erds.ua.config.TestSecurityConfiguration;
import pl.envelo.erds.ua.domain.UserAccount;
import pl.envelo.erds.ua.repository.UserAccountRepository;
import pl.envelo.erds.ua.service.UserAccountService;
import pl.envelo.erds.ua.service.dto.UserAccountDTO;
import pl.envelo.erds.ua.service.mapper.UserAccountMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@SpringBootTest(classes = { UseragentApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserAccountResourceIT {

    private static final String DEFAULT_NCLOAK_ID = "AAAAAAAAAA";
    private static final String UPDATED_NCLOAK_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserAccountRepository userAccountRepositoryMock;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Mock
    private UserAccountService userAccountServiceMock;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccountMockMvc;

    private UserAccount userAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .ncloakId(DEFAULT_NCLOAK_ID)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        return userAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .ncloakId(UPDATED_NCLOAK_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        return userAccount;
    }

    @BeforeEach
    public void initTest() {
        userAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserAccount() throws Exception {
        int databaseSizeBeforeCreate = userAccountRepository.findAll().size();

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        restUserAccountMockMvc.perform(post("/api/user-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate + 1);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getNcloakId()).isEqualTo(DEFAULT_NCLOAK_ID);
        assertThat(testUserAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAccount.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserAccount.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserAccount.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createUserAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userAccountRepository.findAll().size();

        // Create the UserAccount with an existing ID
        userAccount.setId(1L);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccountMockMvc.perform(post("/api/user-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserAccounts() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        // Get all the userAccountList
        restUserAccountMockMvc.perform(get("/api/user-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].ncloakId").value(hasItem(DEFAULT_NCLOAK_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAccountMockMvc.perform(get("/api/user-accounts?eagerload=true"))
            .andExpect(status().isOk());

        verify(userAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAccountMockMvc.perform(get("/api/user-accounts?eagerload=true"))
            .andExpect(status().isOk());

        verify(userAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        // Get the userAccount
        restUserAccountMockMvc.perform(get("/api/user-accounts/{id}", userAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccount.getId().intValue()))
            .andExpect(jsonPath("$.ncloakId").value(DEFAULT_NCLOAK_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAccount() throws Exception {
        // Get the userAccount
        restUserAccountMockMvc.perform(get("/api/user-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).get();
        // Disconnect from session so that the updates on updatedUserAccount are not directly saved in db
        em.detach(updatedUserAccount);
        updatedUserAccount
            .ncloakId(UPDATED_NCLOAK_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(updatedUserAccount);

        restUserAccountMockMvc.perform(put("/api/user-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userAccountDTO)))
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getNcloakId()).isEqualTo(UPDATED_NCLOAK_ID);
        assertThat(testUserAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserAccount.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserAccount.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserAccount.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc.perform(put("/api/user-accounts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.saveAndFlush(userAccount);

        int databaseSizeBeforeDelete = userAccountRepository.findAll().size();

        // Delete the userAccount
        restUserAccountMockMvc.perform(delete("/api/user-accounts/{id}", userAccount.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
