package com.evgeny.innowisetasks.Repository;

import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentCardsRepository extends JpaRepository<PaymentCardsEntity, Long> {

    //Get all Cards by User id
    List<PaymentCardsEntity> findAllByUserId(Long userId);

}
