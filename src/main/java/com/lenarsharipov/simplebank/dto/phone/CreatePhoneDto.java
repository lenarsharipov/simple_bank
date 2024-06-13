package com.lenarsharipov.simplebank.dto.phone;

import com.lenarsharipov.simplebank.validation.UniquePhone;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

public record CreatePhoneDto(
    @UniquePhone
    @Digits(integer = 10, fraction = 0)
    @NotBlank(message = "Phone cannot be null or blank")
    String phone
) {
}
