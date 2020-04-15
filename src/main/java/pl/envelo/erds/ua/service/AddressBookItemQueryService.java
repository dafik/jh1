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

import pl.envelo.erds.ua.domain.AddressBookItem;
import pl.envelo.erds.ua.domain.*; // for static metamodels
import pl.envelo.erds.ua.repository.AddressBookItemRepository;
import pl.envelo.erds.ua.service.dto.AddressBookItemCriteria;
import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;
import pl.envelo.erds.ua.service.mapper.AddressBookItemMapper;

/**
 * Service for executing complex queries for {@link AddressBookItem} entities in the database.
 * The main input is a {@link AddressBookItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddressBookItemDTO} or a {@link Page} of {@link AddressBookItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressBookItemQueryService extends QueryService<AddressBookItem> {

    private final Logger log = LoggerFactory.getLogger(AddressBookItemQueryService.class);

    private final AddressBookItemRepository addressBookItemRepository;

    private final AddressBookItemMapper addressBookItemMapper;

    public AddressBookItemQueryService(AddressBookItemRepository addressBookItemRepository, AddressBookItemMapper addressBookItemMapper) {
        this.addressBookItemRepository = addressBookItemRepository;
        this.addressBookItemMapper = addressBookItemMapper;
    }

    /**
     * Return a {@link List} of {@link AddressBookItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressBookItemDTO> findByCriteria(AddressBookItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AddressBookItem> specification = createSpecification(criteria);
        return addressBookItemMapper.toDto(addressBookItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddressBookItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressBookItemDTO> findByCriteria(AddressBookItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AddressBookItem> specification = createSpecification(criteria);
        return addressBookItemRepository.findAll(specification, page)
            .map(addressBookItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddressBookItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AddressBookItem> specification = createSpecification(criteria);
        return addressBookItemRepository.count(specification);
    }

    /**
     * Function to convert {@link AddressBookItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AddressBookItem> createSpecification(AddressBookItemCriteria criteria) {
        Specification<AddressBookItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AddressBookItem_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), AddressBookItem_.uid));
            }
            if (criteria.getSchema() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchema(), AddressBookItem_.schema));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), AddressBookItem_.label));
            }
            if (criteria.getUserAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserAccountId(),
                    root -> root.join(AddressBookItem_.userAccount, JoinType.LEFT).get(UserAccount_.id)));
            }
        }
        return specification;
    }
}
