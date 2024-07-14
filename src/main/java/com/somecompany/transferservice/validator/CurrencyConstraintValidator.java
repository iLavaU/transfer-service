package com.somecompany.transferservice.validator;

import com.somecompany.transferservice.annotation.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class CurrencyConstraintValidator implements ConstraintValidator<ValidCurrency, String> {

    Set<String> currencies = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toCollection(HashSet::new));

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = currencies.contains(currency.toUpperCase());
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(String.format("Currency %s not existent.", currency))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
