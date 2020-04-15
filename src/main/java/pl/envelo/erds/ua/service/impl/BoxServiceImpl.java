package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.BoxService;
import pl.envelo.erds.ua.domain.Box;
import pl.envelo.erds.ua.repository.BoxRepository;
import pl.envelo.erds.ua.service.dto.BoxDTO;
import pl.envelo.erds.ua.service.mapper.BoxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Box}.
 */
@Service
@Transactional
public class BoxServiceImpl implements BoxService {

    private final Logger log = LoggerFactory.getLogger(BoxServiceImpl.class);

    private final BoxRepository boxRepository;

    private final BoxMapper boxMapper;

    public BoxServiceImpl(BoxRepository boxRepository, BoxMapper boxMapper) {
        this.boxRepository = boxRepository;
        this.boxMapper = boxMapper;
    }

    /**
     * Save a box.
     *
     * @param boxDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BoxDTO save(BoxDTO boxDTO) {
        log.debug("Request to save Box : {}", boxDTO);
        Box box = boxMapper.toEntity(boxDTO);
        box = boxRepository.save(box);
        return boxMapper.toDto(box);
    }

    /**
     * Get all the boxes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BoxDTO> findAll() {
        log.debug("Request to get all Boxes");
        return boxRepository.findAll().stream()
            .map(boxMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one box by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BoxDTO> findOne(Long id) {
        log.debug("Request to get Box : {}", id);
        return boxRepository.findById(id)
            .map(boxMapper::toDto);
    }

    /**
     * Delete the box by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Box : {}", id);
        boxRepository.deleteById(id);
    }
}
