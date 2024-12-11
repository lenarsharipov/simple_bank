package com.lenarsharipov.simplebank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@Entity
@Audited
public class Phone
        extends BaseEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Builder.Default
    private String externalId = UUID.randomUUID().toString();

    private String number;

    public static Phone of(String number) {
        return Phone.builder()
                .number(number)
                .build();
    }
}
