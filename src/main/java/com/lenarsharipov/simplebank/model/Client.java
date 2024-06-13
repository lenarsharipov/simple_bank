package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString(exclude = {"emails", "phones"})
@EqualsAndHashCode(exclude = {"emails", "phones"}, callSuper = false)
@Builder
@Entity
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    private User user;

    private String fullName;

    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    private Account account;

    @Builder.Default
    @OneToMany(cascade = {PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false)
    private List<Email> emails = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = {PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false)
    private List<Phone> phones = new ArrayList<>();
}
