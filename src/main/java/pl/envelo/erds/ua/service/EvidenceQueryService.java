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

import pl.envelo.erds.ua.domain.Evidence;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.EvidenceRepository;
import pl.envelo.erds.ua.service.dto.EvidenceCriteria;
import pl.envelo.erds.ua.service.dto.EvidenceDTO;
import pl.envelo.erds.ua.service.mapper.EvidenceMapper;

/**
 * Service for executing complex queries for {@link Evidence} entities in the database.
 * The main input is a {@link EvidenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EvidenceDTO} or a {@link Page} of {@link EvidenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvidenceQueryService extends QueryService<Evidence> {

    private final Logger log = LoggerFactory.getLogger(EvidenceQueryService.class);

    private final EvidenceRepository evidenceRepository;

    private final EvidenceMapper evidenceMapper;

    public EvidenceQueryService(EvidenceRepository evidenceRepository, EvidenceMapper evidenceMapper) {
        this.evidenceRepository = evidenceRepository;
        this.evidenceMapper = evidenceMapper;
    }

    /**
     * Return a {@link List} of {@link EvidenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EvidenceDTO> findByCriteria(EvidenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evidence> specification = createSpecification(criteria);
        return evidenceMapper.toDto(evidenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EvidenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvidenceDTO> findByCriteria(EvidenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evidence> specification = createSpecification(criteria);
        return evidenceRepository.findAll(specification, page)
            .map(evidenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvidenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evidence> specification = createSpecification(criteria);
        return evidenceRepository.count(specification);
    }

    /**
     * Function to convert {@link EvidenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evidence> createSpecification(EvidenceCriteria criteria) {
        Specification<Evidence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evidence_.id));
            }
            if (criteria.getEan() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEan(), Evidence_.ean));
            }
            if (criteria.getErdsId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErdsId(), Evidence_.erdsId));
            }
            if (criteria.getProcessId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProcessId(), Evidence_.processId));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Evidence_.date));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Evidence_.type));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Evidence_.location));
            }
            if (criteria.getBoxId() != null) {
                specification = specification.and(buildSpecification(criteria.getBoxId(),
                    root -> root.join(Evidence_.box, JoinType.LEFT).get(Box_.id)));
            }
        }
        return specification;
    }
}
