package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version = 0L;

    private BigDecimal initialDeposit;

    /**
     * Баланс счета клиента не может уходить в минус ни при каких обстоятельствах.
     * На “банковском счету” должна быть какая-то изначальная сумма.
     */
    private BigDecimal balance;
}