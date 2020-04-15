package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.Evidence;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Evidence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long>, JpaSpecificationExecutor<Evidence> {
}
