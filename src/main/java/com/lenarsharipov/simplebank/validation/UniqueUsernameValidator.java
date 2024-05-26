package com.lenarsharipov.simplebank.validation;

import com.lenarsharipov.simplebank.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String login,
                           ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(login);
    }
}