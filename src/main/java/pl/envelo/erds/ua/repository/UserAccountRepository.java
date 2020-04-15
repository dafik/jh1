package pl.envelo.erds.ua.repository;

import pl.envelo.erds.ua.domain.UserAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserAccount entity.
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query(value = "select distinct userAccount from UserAccount userAccount left join fetch userAccount.boxes",
        countQuery = "select count(distinct userAccount) from UserAccount userAccount")
    Page<UserAccount> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userAccount from UserAccount userAccount left join fetch userAccount.boxes")
    List<UserAccount> findAllWithEagerRelationships();

    @Query("select userAccount from UserAccount userAccount left join fetch userAccount.boxes where userAccount.id =:id")
    Optional<UserAccount> findOneWithEagerRelationships(@Param("id") Long id);
}
