package pl.envelo.erds.ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.envelo.erds.ua.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
