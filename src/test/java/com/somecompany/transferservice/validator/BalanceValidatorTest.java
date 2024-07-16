package com.somecompany.transferservice.validator;

import com.somecompany.transferservice.dto.BalanceValidationDto;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.validator.impl.BalanceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.somecompany.transferservice.validator.impl.BalanceValidator.ERROR_MSG_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BalanceValidatorTest {

    BalanceValidator balanceValidator = new BalanceValidator();
    Account testAccount = new Account();
    BigDecimal testTransferAmount = new BigDecimal(100);

    @Test
    public void testInsufficientBalance() {
        testAccount.setBalance(BigDecimal.valueOf(10));
        Optional<String> result = this.balanceValidator.validate(BalanceValidationDto.builder()
            .account(testAccount)
            .transferAmount(testTransferAmount)
            .build());
        assertTrue(result.isPresent());
        assertEquals(String.format(ERROR_MSG_TEMPLATE, testAccount.getBalance(), testTransferAmount), result.get());
    }

    @Test
    public void testSufficientBalanceGreaterBalance() {
        testAccount.setBalance(BigDecimal.valueOf(101));
        Optional<String> result = this.balanceValidator.validate(BalanceValidationDto.builder()
                .account(testAccount)
                .transferAmount(testTransferAmount)
                .build());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSufficientBalanceEqualBalance() {
        testAccount.setBalance(BigDecimal.valueOf(100));
        Optional<String> result = this.balanceValidator.validate(BalanceValidationDto.builder()
                .account(testAccount)
                .transferAmount(testTransferAmount)
                .build());
        assertTrue(result.isEmpty());
    }
}
