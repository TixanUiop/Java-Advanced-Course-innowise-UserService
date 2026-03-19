package com.innowise.innowisetasks.service;

import com.innowise.innowisetasks.dto.CreatePaymentCardsDTO;
import com.innowise.innowisetasks.dto.PaymentCardsDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaymentCardService {

    PaymentCardsDTO create(CreatePaymentCardsDTO dto);

    PaymentCardsDTO getById(Long id);

    Page<PaymentCardsDTO> getAll(Pageable pageable);

    List<PaymentCardsDTO> getAllByUserId(Long userId);

    PaymentCardsDTO update(PaymentCardsDTO dto);

    Boolean activate(Long id);

    Boolean deactivate(Long id);

    PaymentCardsDTO delete(Long id);
}
