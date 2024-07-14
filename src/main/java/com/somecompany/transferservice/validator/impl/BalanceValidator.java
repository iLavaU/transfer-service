package com.somecompany.transferservice.validator.impl;

import com.somecompany.transferservice.dto.transfer.BalanceValidationDto;
import com.somecompany.transferservice.validator.ConditionValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class BalanceValidator implements ConditionValidator<BalanceValidationDto> {

    public static final String ERROR_MSG_TEMPLATE = "Account balance %s is less than transfer amount %s.";

    @Override
    public Optional<String> validate(BalanceValidationDto input) {
        BigDecimal transferAmount = input.getTransferAmount();
        BigDecimal accountBalance = input.getAccount().getBalance();
        if (transferAmount.compareTo(accountBalance) > 0) {
            return Optional.of(String.format(ERROR_MSG_TEMPLATE, accountBalance, transferAmount));
        }
        return Optional.empty();
    }
}
