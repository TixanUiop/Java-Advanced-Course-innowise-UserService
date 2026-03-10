package com.evgeny.innowisetasks.DTO;

import com.evgeny.innowisetasks.Entity.PaymentCardsEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Past
    private LocalDate birthDate;

    @Email
    private String email;

    private Boolean active = true;

    private List<PaymentCardsEntity> cards;
}
