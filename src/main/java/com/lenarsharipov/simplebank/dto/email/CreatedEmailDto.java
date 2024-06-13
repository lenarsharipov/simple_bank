package com.lenarsharipov.simplebank.dto.email;

import lombok.Builder;

@Builder
public record CreatedEmailDto(
        String email) {

}
