package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Email> emails = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Phone> phones = new ArrayList<>();

    public void addEmail(Email email) {
        this.emails.add(email);
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }

    public boolean hasPhone(Phone phone) {
        return phones.contains(phone);
    }

    public boolean hasEmail(Email email) {
        return emails.contains(email);
    }

    public void increaseBalance(BigDecimal amount) {
        BigDecimal balance = this.account.getBalance();
        balance = balance.add(amount);
        this.account.setBalance(balance);
    }

    public void decreaseBalance(BigDecimal amount) {
        BigDecimal balance = this.account.getBalance();
        balance = balance.subtract(amount);
        this.account.setBalance(balance);
    }
}
