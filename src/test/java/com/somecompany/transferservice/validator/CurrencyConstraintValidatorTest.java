package com.somecompany.transferservice.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConstraintValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;
    @Mock
    CurrencyConstraintValidator cc;

    @Test
    void isValid() {
        cc.currencies = Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toCollection(HashSet::new));
        when(cc.isValid(any(), any())).thenCallRealMethod();
        assertTrue(cc.isValid("USD", constraintValidatorContext));
    }

    @Test
    void isInValid() {
        cc.currencies = Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toCollection(HashSet::new));
        when(cc.isValid(any(), any())).thenCallRealMethod();
        assertFalse(cc.isValid("asd", constraintValidatorContext));
    }
}
