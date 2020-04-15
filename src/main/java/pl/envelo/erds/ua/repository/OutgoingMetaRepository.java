package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.OutgoingMeta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OutgoingMeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutgoingMetaRepository extends JpaRepository<OutgoingMeta, Long>, JpaSpecificationExecutor<OutgoingMeta> {
}
