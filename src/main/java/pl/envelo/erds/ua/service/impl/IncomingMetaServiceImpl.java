package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.IncomingMetaService;
import pl.envelo.erds.ua.domain.IncomingMeta;
import pl.envelo.erds.ua.repository.IncomingMetaRepository;
import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;
import pl.envelo.erds.ua.service.mapper.IncomingMetaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link IncomingMeta}.
 */
@Service
@Transactional
public class IncomingMetaServiceImpl implements IncomingMetaService {

    private final Logger log = LoggerFactory.getLogger(IncomingMetaServiceImpl.class);

    private final IncomingMetaRepository incomingMetaRepository;

    private final IncomingMetaMapper incomingMetaMapper;

    public IncomingMetaServiceImpl(IncomingMetaRepository incomingMetaRepository, IncomingMetaMapper incomingMetaMapper) {
        this.incomingMetaRepository = incomingMetaRepository;
        this.incomingMetaMapper = incomingMetaMapper;
    }

    /**
     * Save a incomingMeta.
     *
     * @param incomingMetaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public IncomingMetaDTO save(IncomingMetaDTO incomingMetaDTO) {
        log.debug("Request to save IncomingMeta : {}", incomingMetaDTO);
        IncomingMeta incomingMeta = incomingMetaMapper.toEntity(incomingMetaDTO);
        incomingMeta = incomingMetaRepository.save(incomingMeta);
        return incomingMetaMapper.toDto(incomingMeta);
    }

    /**
     * Get all the incomingMetas.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<IncomingMetaDTO> findAll() {
        log.debug("Request to get all IncomingMetas");
        return incomingMetaRepository.findAll().stream()
            .map(incomingMetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     *  Get all the incomingMetas where LetterGroup is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<IncomingMetaDTO> findAllWhereLetterGroupIsNull() {
        log.debug("Request to get all incomingMetas where LetterGroup is null");
        return StreamSupport
            .stream(incomingMetaRepository.findAll().spliterator(), false)
            .filter(incomingMeta -> incomingMeta.getLetterGroup() == null)
            .map(incomingMetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one incomingMeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IncomingMetaDTO> findOne(Long id) {
        log.debug("Request to get IncomingMeta : {}", id);
        return incomingMetaRepository.findById(id)
            .map(incomingMetaMapper::toDto);
    }

    /**
     * Delete the incomingMeta by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncomingMeta : {}", id);
        incomingMetaRepository.deleteById(id);
    }
}
