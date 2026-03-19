package com.innowise.innowisetasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCardsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
