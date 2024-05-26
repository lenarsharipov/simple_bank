package com.lenarsharipov.simplebank.dto.email;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreatedEmailDto {

    String email;
}
