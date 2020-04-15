package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.LetterService;
import pl.envelo.erds.ua.domain.Letter;
import pl.envelo.erds.ua.repository.LetterRepository;
import pl.envelo.erds.ua.service.dto.LetterDTO;
import pl.envelo.erds.ua.service.mapper.LetterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Letter}.
 */
@Service
@Transactional
public class LetterServiceImpl implements LetterService {

    private final Logger log = LoggerFactory.getLogger(LetterServiceImpl.class);

    private final LetterRepository letterRepository;

    private final LetterMapper letterMapper;

    public LetterServiceImpl(LetterRepository letterRepository, LetterMapper letterMapper) {
        this.letterRepository = letterRepository;
        this.letterMapper = letterMapper;
    }

    /**
     * Save a letter.
     *
     * @param letterDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LetterDTO save(LetterDTO letterDTO) {
        log.debug("Request to save Letter : {}", letterDTO);
        Letter letter = letterMapper.toEntity(letterDTO);
        letter = letterRepository.save(letter);
        return letterMapper.toDto(letter);
    }

    /**
     * Get all the letters.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LetterDTO> findAll() {
        log.debug("Request to get all Letters");
        return letterRepository.findAll().stream()
            .map(letterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one letter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LetterDTO> findOne(Long id) {
        log.debug("Request to get Letter : {}", id);
        return letterRepository.findById(id)
            .map(letterMapper::toDto);
    }

    /**
     * Delete the letter by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Letter : {}", id);
        letterRepository.deleteById(id);
    }
}
