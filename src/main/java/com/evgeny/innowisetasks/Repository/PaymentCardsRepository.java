package com.evgeny.innowisetasks.Repository;

import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import com.evgeny.innowisetasks.Entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCardsRepository extends JpaRepository<PaymentCardsEntity, Long> {

    //Get all Cards by User id
    List<PaymentCardsEntity> findAllByUserId(Long userId);

    long countByUserId(Long userId);

    //JPQL
    @Modifying
    @Query("UPDATE UserEntity u SET u.active = :active WHERE u.id = :id")
    Boolean updateActiveStatusJPQL(@Param("id") Long id, @Param("active") boolean active);

    //JPQL
    @Modifying
    @Query("UPDATE PaymentCardsEntity p SET p.active = :active WHERE p.id = :id")
    int updateActiveStatusPayJPQL(@Param("id") Long id, @Param("active") boolean active);
}
