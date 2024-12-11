package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Audited
public class Account
        extends BaseEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotAudited
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