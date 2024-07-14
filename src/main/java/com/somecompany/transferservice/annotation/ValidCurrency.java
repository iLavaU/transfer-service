package com.somecompany.transferservice.annotation;

import com.somecompany.transferservice.validator.CurrencyConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrencyConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidCurrency {
    String message() default "Invalid currency.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
