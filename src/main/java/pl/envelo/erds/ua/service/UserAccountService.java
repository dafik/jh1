package pl.envelo.erds.ua.service;

import pl.envelo.erds.ua.service.dto.UserAccountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pl.envelo.erds.ua.domain.UserAccount}.
 */
public interface UserAccountService {

    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    UserAccountDTO save(UserAccountDTO userAccountDTO);

    /**
     * Get all the userAccounts.
     *
     * @return the list of entities.
     */
    List<UserAccountDTO> findAll();

    /**
     * Get all the userAccounts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<UserAccountDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccountDTO> findOne(Long id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
