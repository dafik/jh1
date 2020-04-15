package pl.envelo.erds.ua.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pl.envelo.erds.ua.domain.Letter;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.LetterRepository;
import pl.envelo.erds.ua.service.dto.LetterCriteria;
import pl.envelo.erds.ua.service.dto.LetterDTO;
import pl.envelo.erds.ua.service.mapper.LetterMapper;

/**
 * Service for executing complex queries for {@link Letter} entities in the database.
 * The main input is a {@link LetterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LetterDTO} or a {@link Page} of {@link LetterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LetterQueryService extends QueryService<Letter> {

    private final Logger log = LoggerFactory.getLogger(LetterQueryService.class);

    private final LetterRepository letterRepository;

    private final LetterMapper letterMapper;

    public LetterQueryService(LetterRepository letterRepository, LetterMapper letterMapper) {
        this.letterRepository = letterRepository;
        this.letterMapper = letterMapper;
    }

    /**
     * Return a {@link List} of {@link LetterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LetterDTO> findByCriteria(LetterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Letter> specification = createSpecification(criteria);
        return letterMapper.toDto(letterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LetterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LetterDTO> findByCriteria(LetterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Letter> specification = createSpecification(criteria);
        return letterRepository.findAll(specification, page)
            .map(letterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LetterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Letter> specification = createSpecification(criteria);
        return letterRepository.count(specification);
    }

    /**
     * Function to convert {@link LetterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Letter> createSpecification(LetterCriteria criteria) {
        Specification<Letter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Letter_.id));
            }
            if (criteria.getEan() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEan(), Letter_.ean));
            }
            if (criteria.getErdsId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErdsId(), Letter_.erdsId));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(),
                    root -> root.join(Letter_.sender, JoinType.LEFT).get(Actor_.id)));
            }
            if (criteria.getReceipientId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceipientId(),
                    root -> root.join(Letter_.receipient, JoinType.LEFT).get(Actor_.id)));
            }
            if (criteria.getLetterGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getLetterGroupId(),
                    root -> root.join(Letter_.letterGroup, JoinType.LEFT).get(LetterGroup_.id)));
            }
        }
        return specification;
    }
}
