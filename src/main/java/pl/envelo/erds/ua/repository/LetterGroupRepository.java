package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.LetterGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LetterGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LetterGroupRepository extends JpaRepository<LetterGroup, Long>, JpaSpecificationExecutor<LetterGroup> {
}
