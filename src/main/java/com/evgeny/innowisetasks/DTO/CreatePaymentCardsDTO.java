package com.evgeny.innowisetasks.DTO;

import com.evgeny.innowisetasks.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentCardsDTO {

    private String number;

    private LocalDate expirationDate;

    private Boolean active;

    private Long userId;
}
