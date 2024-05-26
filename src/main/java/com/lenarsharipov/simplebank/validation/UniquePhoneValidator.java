package com.lenarsharipov.simplebank.validation;

import com.lenarsharipov.simplebank.repository.PhoneRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final PhoneRepository phoneRepository;

    @Override
    public boolean isValid(String phoneNumber,
                           ConstraintValidatorContext context) {
        return !phoneRepository.existsByNumber(phoneNumber);
    }
}
