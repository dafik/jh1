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

import pl.envelo.erds.ua.domain.IncomingMeta;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.IncomingMetaRepository;
import pl.envelo.erds.ua.service.dto.IncomingMetaCriteria;
import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;
import pl.envelo.erds.ua.service.mapper.IncomingMetaMapper;

/**
 * Service for executing complex queries for {@link IncomingMeta} entities in the database.
 * The main input is a {@link IncomingMetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IncomingMetaDTO} or a {@link Page} of {@link IncomingMetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IncomingMetaQueryService extends QueryService<IncomingMeta> {

    private final Logger log = LoggerFactory.getLogger(IncomingMetaQueryService.class);

    private final IncomingMetaRepository incomingMetaRepository;

    private final IncomingMetaMapper incomingMetaMapper;

    public IncomingMetaQueryService(IncomingMetaRepository incomingMetaRepository, IncomingMetaMapper incomingMetaMapper) {
        this.incomingMetaRepository = incomingMetaRepository;
        this.incomingMetaMapper = incomingMetaMapper;
    }

    /**
     * Return a {@link List} of {@link IncomingMetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IncomingMetaDTO> findByCriteria(IncomingMetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IncomingMeta> specification = createSpecification(criteria);
        return incomingMetaMapper.toDto(incomingMetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IncomingMetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IncomingMetaDTO> findByCriteria(IncomingMetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IncomingMeta> specification = createSpecification(criteria);
        return incomingMetaRepository.findAll(specification, page)
            .map(incomingMetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IncomingMetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IncomingMeta> specification = createSpecification(criteria);
        return incomingMetaRepository.count(specification);
    }

    /**
     * Function to convert {@link IncomingMetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IncomingMeta> createSpecification(IncomingMetaCriteria criteria) {
        Specification<IncomingMeta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IncomingMeta_.id));
            }
            if (criteria.getReceivedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivedAt(), IncomingMeta_.receivedAt));
            }
            if (criteria.getReadAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReadAt(), IncomingMeta_.readAt));
            }
            if (criteria.getLetterGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getLetterGroupId(),
                    root -> root.join(IncomingMeta_.letterGroup, JoinType.LEFT).get(LetterGroup_.id)));
            }
        }
        return specification;
    }
}
