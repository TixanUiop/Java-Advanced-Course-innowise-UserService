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
public class PaymentCardsDTO {

    private Long id;

    private String number;

    private String holder;

    private LocalDate expirationDate;

    private Boolean active;

    private UserEntity user;
}
