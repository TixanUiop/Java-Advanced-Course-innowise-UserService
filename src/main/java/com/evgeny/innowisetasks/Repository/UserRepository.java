package com.evgeny.innowisetasks.Repository;

import com.evgeny.innowisetasks.Entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
    Optional<UserEntity> findByIdForUpdate(@Param("id") Long id);

    boolean existsByEmail(String email);

    //JPQL
    @Modifying
    @Query("UPDATE UserEntity u SET u.active = :active WHERE u.id = :id")
    int updateActiveStatusJPQL(@Param("id") Long id, @Param("active") boolean active);


    @Query(
            value = "SELECT * FROM users WHERE name LIKE %:name% AND surname LIKE %:surname%",
            countQuery = "SELECT count(*) FROM users WHERE name LIKE %:name% AND surname LIKE %:surname%",
            nativeQuery = true
    )
    Page<UserEntity> findByNameAndSurnameNative(
            @Param("name") String name,
            @Param("surname") String surname,
            Pageable pageable
    );
}
