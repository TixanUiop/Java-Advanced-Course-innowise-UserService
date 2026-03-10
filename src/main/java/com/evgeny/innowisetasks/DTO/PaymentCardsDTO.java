package com.evgeny.innowisetasks.DTO;

import com.evgeny.innowisetasks.Entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull
    private Long id;

    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String number;

    @NotBlank
    private String holder;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Boolean active;
}
