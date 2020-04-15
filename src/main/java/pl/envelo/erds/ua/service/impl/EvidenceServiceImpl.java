package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.EvidenceService;
import pl.envelo.erds.ua.domain.Evidence;
import pl.envelo.erds.ua.repository.EvidenceRepository;
import pl.envelo.erds.ua.service.dto.EvidenceDTO;
import pl.envelo.erds.ua.service.mapper.EvidenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Evidence}.
 */
@Service
@Transactional
public class EvidenceServiceImpl implements EvidenceService {

    private final Logger log = LoggerFactory.getLogger(EvidenceServiceImpl.class);

    private final EvidenceRepository evidenceRepository;

    private final EvidenceMapper evidenceMapper;

    public EvidenceServiceImpl(EvidenceRepository evidenceRepository, EvidenceMapper evidenceMapper) {
        this.evidenceRepository = evidenceRepository;
        this.evidenceMapper = evidenceMapper;
    }

    /**
     * Save a evidence.
     *
     * @param evidenceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EvidenceDTO save(EvidenceDTO evidenceDTO) {
        log.debug("Request to save Evidence : {}", evidenceDTO);
        Evidence evidence = evidenceMapper.toEntity(evidenceDTO);
        evidence = evidenceRepository.save(evidence);
        return evidenceMapper.toDto(evidence);
    }

    /**
     * Get all the evidences.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EvidenceDTO> findAll() {
        log.debug("Request to get all Evidences");
        return evidenceRepository.findAll().stream()
            .map(evidenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one evidence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EvidenceDTO> findOne(Long id) {
        log.debug("Request to get Evidence : {}", id);
        return evidenceRepository.findById(id)
            .map(evidenceMapper::toDto);
    }

    /**
     * Delete the evidence by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evidence : {}", id);
        evidenceRepository.deleteById(id);
    }
}
