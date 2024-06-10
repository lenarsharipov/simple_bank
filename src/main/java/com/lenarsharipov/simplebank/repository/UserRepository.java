package com.lenarsharipov.simplebank.repository;

import com.lenarsharipov.simplebank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String login);

    boolean existsByUsername(String login);
}
