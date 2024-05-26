package com.lenarsharipov.simplebank.repository;

import com.lenarsharipov.simplebank.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

    boolean existsByAddress(String emailAddress);

}
