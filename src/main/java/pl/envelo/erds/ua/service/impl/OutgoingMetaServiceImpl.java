package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.OutgoingMetaService;
import pl.envelo.erds.ua.domain.OutgoingMeta;
import pl.envelo.erds.ua.repository.OutgoingMetaRepository;
import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;
import pl.envelo.erds.ua.service.mapper.OutgoingMetaMapper;
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
 * Service Implementation for managing {@link OutgoingMeta}.
 */
@Service
@Transactional
public class OutgoingMetaServiceImpl implements OutgoingMetaService {

    private final Logger log = LoggerFactory.getLogger(OutgoingMetaServiceImpl.class);

    private final OutgoingMetaRepository outgoingMetaRepository;

    private final OutgoingMetaMapper outgoingMetaMapper;

    public OutgoingMetaServiceImpl(OutgoingMetaRepository outgoingMetaRepository, OutgoingMetaMapper outgoingMetaMapper) {
        this.outgoingMetaRepository = outgoingMetaRepository;
        this.outgoingMetaMapper = outgoingMetaMapper;
    }

    /**
     * Save a outgoingMeta.
     *
     * @param outgoingMetaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OutgoingMetaDTO save(OutgoingMetaDTO outgoingMetaDTO) {
        log.debug("Request to save OutgoingMeta : {}", outgoingMetaDTO);
        OutgoingMeta outgoingMeta = outgoingMetaMapper.toEntity(outgoingMetaDTO);
        outgoingMeta = outgoingMetaRepository.save(outgoingMeta);
        return outgoingMetaMapper.toDto(outgoingMeta);
    }

    /**
     * Get all the outgoingMetas.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OutgoingMetaDTO> findAll() {
        log.debug("Request to get all OutgoingMetas");
        return outgoingMetaRepository.findAll().stream()
            .map(outgoingMetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     *  Get all the outgoingMetas where LetterGroup is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<OutgoingMetaDTO> findAllWhereLetterGroupIsNull() {
        log.debug("Request to get all outgoingMetas where LetterGroup is null");
        return StreamSupport
            .stream(outgoingMetaRepository.findAll().spliterator(), false)
            .filter(outgoingMeta -> outgoingMeta.getLetterGroup() == null)
            .map(outgoingMetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one outgoingMeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OutgoingMetaDTO> findOne(Long id) {
        log.debug("Request to get OutgoingMeta : {}", id);
        return outgoingMetaRepository.findById(id)
            .map(outgoingMetaMapper::toDto);
    }

    /**
     * Delete the outgoingMeta by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutgoingMeta : {}", id);
        outgoingMetaRepository.deleteById(id);
    }
}
