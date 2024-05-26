package com.lenarsharipov.simplebank.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class LegalAgeValidator implements ConstraintValidator<LegalAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate,
                           ConstraintValidatorContext context) {
        int fullYears = getFullYears(birthDate);
        return fullYears >= 18 && fullYears < 120;
    }

    private int getFullYears(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
