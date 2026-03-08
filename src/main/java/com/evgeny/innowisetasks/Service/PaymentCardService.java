package com.evgeny.innowisetasks.Service;

import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaymentCardService {

    PaymentCardsDTO create(CreatePaymentCardsDTO dto, Long userId);

    PaymentCardsDTO getById(Long id);

    Page<PaymentCardsDTO> getAll(Pageable pageable, String name, String surname);

    List<PaymentCardsDTO> getAllByUserId(Long userId);

    PaymentCardsDTO update(Long id, PaymentCardsDTO dto);

    Boolean activate(Long id);

    Boolean deactivate(Long id);
}
