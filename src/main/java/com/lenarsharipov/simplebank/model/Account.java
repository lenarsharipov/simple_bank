package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
public class Account
        extends BaseEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Builder.Default
    private Long version = 0L;

    private BigDecimal initialDeposit;

    /**
     * Баланс счета клиента не может уходить в минус ни при каких обстоятельствах.
     * На “банковском счету” должна быть какая-то изначальная сумма.
     */
    private BigDecimal balance;
}