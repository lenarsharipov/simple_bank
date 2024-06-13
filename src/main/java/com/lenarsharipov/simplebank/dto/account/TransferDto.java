package com.lenarsharipov.simplebank.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferDto(
        @Schema(description = "Transferred amount", example = "10 000")
        @Digits(integer = 5, fraction = 2)
        @Positive(message = "Amount must be > 0")
        @NotNull(message = "Cannot be null")
        BigDecimal amount,

        @Schema(description = "Receiver id", example = "1")
        @Positive
        @NotNull
        Long receiverUserId) {

}
