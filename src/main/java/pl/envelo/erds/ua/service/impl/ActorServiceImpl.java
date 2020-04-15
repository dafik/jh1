package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.ActorService;
import pl.envelo.erds.ua.domain.Actor;
import pl.envelo.erds.ua.repository.ActorRepository;
import pl.envelo.erds.ua.service.dto.ActorDTO;
import pl.envelo.erds.ua.service.mapper.ActorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Actor}.
 */
@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ActorDTO save(ActorDTO actorDTO) {
        log.debug("Request to save Actor : {}", actorDTO);
        Actor actor = actorMapper.toEntity(actorDTO);
        actor = actorRepository.save(actor);
        return actorMapper.toDto(actor);
    }

    /**
     * Get all the actors.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActorDTO> findAll() {
        log.debug("Request to get all Actors");
        return actorRepository.findAll().stream()
            .map(actorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one actor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ActorDTO> findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        return actorRepository.findById(id)
            .map(actorMapper::toDto);
    }

    /**
     * Delete the actor by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.deleteById(id);
    }
}
