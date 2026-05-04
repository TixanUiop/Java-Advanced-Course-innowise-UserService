package com.innowise.userservice.repository;

import com.innowise.userservice.entity.UserEntity;
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

    Optional<UserEntity> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
    Optional<UserEntity> findByIdForUpdate(@Param("id") Long id);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"cards"})
    Optional<UserEntity> findById(Long id);

    @EntityGraph(attributePaths = {"cards"})
    Page<UserEntity> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE UserEntity u SET u.active = :active WHERE u.id = :id")
    int updateActiveStatusJPQL(@Param("id") Long id, @Param("active") boolean active);



}
