package com.innowise.userservice.dto;

import jakarta.validation.constraints.Future;
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
public class CreatePaymentCardsDTO {

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must contain 16 digits")
    private String number;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @NotNull
    private Boolean active;

    @NotNull
    private Long userId;

}
