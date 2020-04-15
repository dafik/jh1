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

import pl.envelo.erds.ua.domain.Actor;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.ActorRepository;
import pl.envelo.erds.ua.service.dto.ActorCriteria;
import pl.envelo.erds.ua.service.dto.ActorDTO;
import pl.envelo.erds.ua.service.mapper.ActorMapper;

/**
 * Service for executing complex queries for {@link Actor} entities in the database.
 * The main input is a {@link ActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActorDTO} or a {@link Page} of {@link ActorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActorQueryService extends QueryService<Actor> {

    private final Logger log = LoggerFactory.getLogger(ActorQueryService.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    public ActorQueryService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    /**
     * Return a {@link List} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActorDTO> findByCriteria(ActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorMapper.toDto(actorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActorDTO> findByCriteria(ActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification, page)
            .map(actorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.count(specification);
    }

    /**
     * Function to convert {@link ActorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Actor> createSpecification(ActorCriteria criteria) {
        Specification<Actor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Actor_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), Actor_.uid));
            }
            if (criteria.getSchema() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchema(), Actor_.schema));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Actor_.label));
            }
        }
        return specification;
    }
}
