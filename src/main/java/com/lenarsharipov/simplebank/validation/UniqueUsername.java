package com.lenarsharipov.simplebank.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueUsername {

    String message() default "Username in use";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
