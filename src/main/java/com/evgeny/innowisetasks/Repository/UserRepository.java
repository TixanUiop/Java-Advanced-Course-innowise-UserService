package com.evgeny.innowisetasks.Repository;

import com.evgeny.innowisetasks.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {


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
