package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.ThreadService;
import pl.envelo.erds.ua.domain.Thread;
import pl.envelo.erds.ua.repository.ThreadRepository;
import pl.envelo.erds.ua.service.dto.ThreadDTO;
import pl.envelo.erds.ua.service.mapper.ThreadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Thread}.
 */
@Service
@Transactional
public class ThreadServiceImpl implements ThreadService {

    private final Logger log = LoggerFactory.getLogger(ThreadServiceImpl.class);

    private final ThreadRepository threadRepository;

    private final ThreadMapper threadMapper;

    public ThreadServiceImpl(ThreadRepository threadRepository, ThreadMapper threadMapper) {
        this.threadRepository = threadRepository;
        this.threadMapper = threadMapper;
    }

    /**
     * Save a thread.
     *
     * @param threadDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ThreadDTO save(ThreadDTO threadDTO) {
        log.debug("Request to save Thread : {}", threadDTO);
        Thread thread = threadMapper.toEntity(threadDTO);
        thread = threadRepository.save(thread);
        return threadMapper.toDto(thread);
    }

    /**
     * Get all the threads.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ThreadDTO> findAll() {
        log.debug("Request to get all Threads");
        return threadRepository.findAll().stream()
            .map(threadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one thread by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadDTO> findOne(Long id) {
        log.debug("Request to get Thread : {}", id);
        return threadRepository.findById(id)
            .map(threadMapper::toDto);
    }

    /**
     * Delete the thread by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Thread : {}", id);
        threadRepository.deleteById(id);
    }
}
