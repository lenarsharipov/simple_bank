package com.lenarsharipov.simplebank.validation;

import com.lenarsharipov.simplebank.repository.EmailRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final EmailRepository emailRepository;

    @Override
    public boolean isValid(String emailAddress,
                           ConstraintValidatorContext context) {
        return !emailRepository.existsByAddress(emailAddress);
    }
}
