package com.lenarsharipov.simplebank.repository;

import com.lenarsharipov.simplebank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository
        extends JpaRepository<Client, Long>,
                JpaSpecificationExecutor<Client> {
}
