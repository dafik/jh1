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

import pl.envelo.erds.ua.domain.LetterGroup;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.LetterGroupRepository;
import pl.envelo.erds.ua.service.dto.LetterGroupCriteria;
import pl.envelo.erds.ua.service.dto.LetterGroupDTO;
import pl.envelo.erds.ua.service.mapper.LetterGroupMapper;

/**
 * Service for executing complex queries for {@link LetterGroup} entities in the database.
 * The main input is a {@link LetterGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LetterGroupDTO} or a {@link Page} of {@link LetterGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LetterGroupQueryService extends QueryService<LetterGroup> {

    private final Logger log = LoggerFactory.getLogger(LetterGroupQueryService.class);

    private final LetterGroupRepository letterGroupRepository;

    private final LetterGroupMapper letterGroupMapper;

    public LetterGroupQueryService(LetterGroupRepository letterGroupRepository, LetterGroupMapper letterGroupMapper) {
        this.letterGroupRepository = letterGroupRepository;
        this.letterGroupMapper = letterGroupMapper;
    }

    /**
     * Return a {@link List} of {@link LetterGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LetterGroupDTO> findByCriteria(LetterGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LetterGroup> specification = createSpecification(criteria);
        return letterGroupMapper.toDto(letterGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LetterGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LetterGroupDTO> findByCriteria(LetterGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LetterGroup> specification = createSpecification(criteria);
        return letterGroupRepository.findAll(specification, page)
            .map(letterGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LetterGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LetterGroup> specification = createSpecification(criteria);
        return letterGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link LetterGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LetterGroup> createSpecification(LetterGroupCriteria criteria) {
        Specification<LetterGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LetterGroup_.id));
            }
            if (criteria.getMsgId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMsgId(), LetterGroup_.msgId));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), LetterGroup_.state));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), LetterGroup_.subject));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), LetterGroup_.createdAt));
            }
            if (criteria.getReplayTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReplayTo(), LetterGroup_.replayTo));
            }
            if (criteria.getInReplayTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInReplayTo(), LetterGroup_.inReplayTo));
            }
            if (criteria.getIncomingMetaId() != null) {
                specification = specification.and(buildSpecification(criteria.getIncomingMetaId(),
                    root -> root.join(LetterGroup_.incomingMeta, JoinType.LEFT).get(IncomingMeta_.id)));
            }
            if (criteria.getOutgoingMetaId() != null) {
                specification = specification.and(buildSpecification(criteria.getOutgoingMetaId(),
                    root -> root.join(LetterGroup_.outgoingMeta, JoinType.LEFT).get(OutgoingMeta_.id)));
            }
            if (criteria.getAttachmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachmentId(),
                    root -> root.join(LetterGroup_.attachments, JoinType.LEFT).get(Attachment_.id)));
            }
            if (criteria.getLetterId() != null) {
                specification = specification.and(buildSpecification(criteria.getLetterId(),
                    root -> root.join(LetterGroup_.letters, JoinType.LEFT).get(Letter_.id)));
            }
            if (criteria.getBoxId() != null) {
                specification = specification.and(buildSpecification(criteria.getBoxId(),
                    root -> root.join(LetterGroup_.box, JoinType.LEFT).get(Box_.id)));
            }
            if (criteria.getThreadId() != null) {
                specification = specification.and(buildSpecification(criteria.getThreadId(),
                    root -> root.join(LetterGroup_.thread, JoinType.LEFT).get(Thread_.id)));
            }
        }
        return specification;
    }
}
