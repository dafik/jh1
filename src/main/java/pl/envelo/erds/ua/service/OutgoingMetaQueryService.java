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

import pl.envelo.erds.ua.domain.OutgoingMeta;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.OutgoingMetaRepository;
import pl.envelo.erds.ua.service.dto.OutgoingMetaCriteria;
import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;
import pl.envelo.erds.ua.service.mapper.OutgoingMetaMapper;

/**
 * Service for executing complex queries for {@link OutgoingMeta} entities in the database.
 * The main input is a {@link OutgoingMetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutgoingMetaDTO} or a {@link Page} of {@link OutgoingMetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutgoingMetaQueryService extends QueryService<OutgoingMeta> {

    private final Logger log = LoggerFactory.getLogger(OutgoingMetaQueryService.class);

    private final OutgoingMetaRepository outgoingMetaRepository;

    private final OutgoingMetaMapper outgoingMetaMapper;

    public OutgoingMetaQueryService(OutgoingMetaRepository outgoingMetaRepository, OutgoingMetaMapper outgoingMetaMapper) {
        this.outgoingMetaRepository = outgoingMetaRepository;
        this.outgoingMetaMapper = outgoingMetaMapper;
    }

    /**
     * Return a {@link List} of {@link OutgoingMetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutgoingMetaDTO> findByCriteria(OutgoingMetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutgoingMeta> specification = createSpecification(criteria);
        return outgoingMetaMapper.toDto(outgoingMetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutgoingMetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutgoingMetaDTO> findByCriteria(OutgoingMetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutgoingMeta> specification = createSpecification(criteria);
        return outgoingMetaRepository.findAll(specification, page)
            .map(outgoingMetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutgoingMetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutgoingMeta> specification = createSpecification(criteria);
        return outgoingMetaRepository.count(specification);
    }

    /**
     * Function to convert {@link OutgoingMetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutgoingMeta> createSpecification(OutgoingMetaCriteria criteria) {
        Specification<OutgoingMeta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutgoingMeta_.id));
            }
            if (criteria.getSendAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendAt(), OutgoingMeta_.sendAt));
            }
            if (criteria.getExpireAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpireAt(), OutgoingMeta_.expireAt));
            }
            if (criteria.getLastEditAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastEditAt(), OutgoingMeta_.lastEditAt));
            }
            if (criteria.getLetterGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getLetterGroupId(),
                    root -> root.join(OutgoingMeta_.letterGroup, JoinType.LEFT).get(LetterGroup_.id)));
            }
        }
        return specification;
    }
}
