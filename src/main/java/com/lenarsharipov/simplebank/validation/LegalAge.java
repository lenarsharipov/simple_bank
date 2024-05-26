package com.lenarsharipov.simplebank.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = LegalAgeValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface LegalAge {

    String message() default "Birth date not correct";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
