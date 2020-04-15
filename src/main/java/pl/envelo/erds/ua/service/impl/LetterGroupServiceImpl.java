package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.LetterGroupService;
import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.repository.LetterGroupRepository;
import pl.envelo.erds.ua.service.dto.LetterGroupDTO;
import pl.envelo.erds.ua.service.mapper.LetterGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LetterGroup}.
 */
@Service
@Transactional
public class LetterGroupServiceImpl implements LetterGroupService {

    private final Logger log = LoggerFactory.getLogger(LetterGroupServiceImpl.class);

    private final LetterGroupRepository letterGroupRepository;

    private final LetterGroupMapper letterGroupMapper;

    public LetterGroupServiceImpl(LetterGroupRepository letterGroupRepository, LetterGroupMapper letterGroupMapper) {
        this.letterGroupRepository = letterGroupRepository;
        this.letterGroupMapper = letterGroupMapper;
    }

    /**
     * Save a letterGroup.
     *
     * @param letterGroupDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LetterGroupDTO save(LetterGroupDTO letterGroupDTO) {
        log.debug("Request to save LetterGroup : {}", letterGroupDTO);
        LetterGroup letterGroup = letterGroupMapper.toEntity(letterGroupDTO);
        letterGroup = letterGroupRepository.save(letterGroup);
        return letterGroupMapper.toDto(letterGroup);
    }

    /**
     * Get all the letterGroups.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LetterGroupDTO> findAll() {
        log.debug("Request to get all LetterGroups");
        return letterGroupRepository.findAll().stream()
            .map(letterGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one letterGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LetterGroupDTO> findOne(Long id) {
        log.debug("Request to get LetterGroup : {}", id);
        return letterGroupRepository.findById(id)
            .map(letterGroupMapper::toDto);
    }

    /**
     * Delete the letterGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LetterGroup : {}", id);
        letterGroupRepository.deleteById(id);
    }
}
