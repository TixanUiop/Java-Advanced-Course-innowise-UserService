package com.innowise.innowisetasks.mapper;

import com.innowise.innowisetasks.dto.CreatePaymentCardsDTO;
import com.innowise.innowisetasks.dto.PaymentCardsDTO;
import com.innowise.innowisetasks.entity.PaymentCardsEntity;
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
