package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.IncomingMeta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the IncomingMeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomingMetaRepository extends JpaRepository<IncomingMeta, Long>, JpaSpecificationExecutor<IncomingMeta> {
}
