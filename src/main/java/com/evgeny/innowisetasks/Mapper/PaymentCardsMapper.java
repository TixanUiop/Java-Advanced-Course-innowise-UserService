package com.evgeny.innowisetasks.Mapper;

import com.evgeny.innowisetasks.DTO.PaymentCardsDTO;
import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PaymentCardsMapper  {

    PaymentCardsDTO toDTO(PaymentCardsEntity card);

    PaymentCardsEntity toEntity(PaymentCardsDTO user);
}
