package com.lenarsharipov.simplebank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String number;
}
