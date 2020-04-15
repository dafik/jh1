package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.Actor;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Actor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {
}
