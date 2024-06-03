package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"emails", "phones"})
@EqualsAndHashCode(exclude = {"emails", "phones"})
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String fullName;

    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    private Account account;

    @Builder.Default
    @OneToMany(cascade = {PERSIST})
    @JoinColumn(name = "user_id")
    private List<Email> emails = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = {PERSIST})
    @JoinColumn(name = "user_id")
    private List<Phone> phones = new ArrayList<>();
}
