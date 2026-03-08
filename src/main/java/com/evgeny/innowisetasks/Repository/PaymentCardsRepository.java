package com.evgeny.innowisetasks.Repository;

import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentCardsRepository extends JpaRepository<PaymentCardsEntity, Long> {

    //Get all Cards by User id
    List<PaymentCardsEntity> findAllByUserId(Long userId);


    //JPQL
    @Modifying
    @Query("UPDATE UserEntity u SET u.active = :active WHERE u.id = :id")
    Boolean updateActiveStatusJPQL(@Param("id") Long id, @Param("active") boolean active);
}
