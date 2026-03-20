package com.evgeny.innowisetasks.Mapper;

import com.evgeny.innowisetasks.DTO.CreatePaymentCardsDTO;
import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring")
public interface PaymentCardsMapper  {

    PaymentCardsDTO toDTO(PaymentCardsEntity card);
    PaymentCardsDTO createToDTO(CreatePaymentCardsDTO card);

    PaymentCardsEntity toEntity(PaymentCardsDTO user);
    PaymentCardsEntity createToEntity(CreatePaymentCardsDTO user);

    List<PaymentCardsDTO> toDtoList(List<PaymentCardsEntity> entities);
}
