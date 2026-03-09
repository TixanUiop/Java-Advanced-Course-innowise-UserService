package com.evgeny.innowisetasks.DTO;

import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    private String name;

    private String surname;

    private LocalDate birthDate;

    private String email;

    private Boolean active = true;
}
