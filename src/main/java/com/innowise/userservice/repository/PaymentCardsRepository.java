package com.innowise.innowisetasks.repository;

import com.innowise.innowisetasks.entity.PaymentCardsEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentCardsRepository extends JpaRepository<PaymentCardsEntity, Long> {

    List<PaymentCardsEntity> findAllByUserId(Long userId);

    long countByUserId(Long userId);

    @Modifying
    @Query("UPDATE UserEntity u SET u.active = :active WHERE u.id = :id")
    Boolean updateActiveStatusJPQL(@Param("id") Long id, @Param("active") boolean active);

    @Modifying
    @Query("UPDATE PaymentCardsEntity p SET p.active = :active WHERE p.id = :id")
    int updateActiveStatusPayJPQL(@Param("id") Long id, @Param("active") boolean active);
}
