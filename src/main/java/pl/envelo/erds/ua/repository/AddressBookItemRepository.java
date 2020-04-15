package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.AddressBookItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AddressBookItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressBookItemRepository extends JpaRepository<AddressBookItem, Long>, JpaSpecificationExecutor<AddressBookItem> {
}
