package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.UserAccountService;
import pl.envelo.erds.ua.domain.UserAccount;
import pl.envelo.erds.ua.repository.UserAccountRepository;
import pl.envelo.erds.ua.service.dto.UserAccountDTO;
import pl.envelo.erds.ua.service.mapper.UserAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UserAccount}.
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserAccountDTO save(UserAccountDTO userAccountDTO) {
        log.debug("Request to save UserAccount : {}", userAccountDTO);
        UserAccount userAccount = userAccountMapper.toEntity(userAccountDTO);
        userAccount = userAccountRepository.save(userAccount);
        return userAccountMapper.toDto(userAccount);
    }

    /**
     * Get all the userAccounts.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAccountDTO> findAll() {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAllWithEagerRelationships().stream()
            .map(userAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userAccounts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserAccountDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userAccountRepository.findAllWithEagerRelationships(pageable).map(userAccountMapper::toDto);
    }

    /**
     * Get one userAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccountDTO> findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findOneWithEagerRelationships(id)
            .map(userAccountMapper::toDto);
    }

    /**
     * Delete the userAccount by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
    }
}
