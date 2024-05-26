package com.lenarsharipov.simplebank.repository;

import com.lenarsharipov.simplebank.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    boolean existsByNumber(String number);

}
