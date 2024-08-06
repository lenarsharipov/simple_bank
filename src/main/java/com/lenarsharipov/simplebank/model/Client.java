package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString(exclude = {"emails", "phones"})
@EqualsAndHashCode(exclude = {"emails", "phones"}, callSuper = false)
@Builder
@Entity
@Audited
public class Client
        extends BaseEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    private User user;

    private String fullName;

    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    private Account account;

    @NotAudited
    @Builder.Default
    @OneToMany(cascade = {PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false)
    private List<Email> emails = new ArrayList<>();

    @NotAudited
    @Builder.Default
    @OneToMany(cascade = PERSIST, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false)
    @MapKey(name = "externalId")
    private Map<String, Phone> phones = new TreeMap<>();
}
