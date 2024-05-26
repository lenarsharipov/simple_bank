package com.lenarsharipov.simplebank.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniquePhoneValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface UniquePhone {

    String message() default "Phone in use";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
