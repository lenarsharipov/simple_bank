package com.lenarsharipov.simplebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String address;
}
