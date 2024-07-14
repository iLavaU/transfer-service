package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.dto.BalanceValidationDTO;
import com.somecompany.transferservice.dto.CurrencyConversionRequestDTO;
import com.somecompany.transferservice.dto.MakeTransferRequestDTO;
import com.somecompany.transferservice.dto.MakeTransferResultDTO;
import com.somecompany.transferservice.exception.AccountNotFoundException;
import com.somecompany.transferservice.exception.InsufficientBalanceException;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.UseCase;
import com.somecompany.transferservice.validator.impl.BalanceValidator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MakeTransferUseCase implements UseCase<MakeTransferRequestDTO, MakeTransferResultDTO> {

    private final MakeCurrencyConversionUseCase conversionUseCase;
    private AccountRepository accountRepository;
    private final BalanceValidator balanceValidator;

    public MakeTransferUseCase(MakeCurrencyConversionUseCase conversionUseCase, BalanceValidator balanceValidator) {
        this.conversionUseCase = conversionUseCase;
        this.balanceValidator = balanceValidator;
    }

    @Override
    public MakeTransferResultDTO execute(MakeTransferRequestDTO input) {

        Account originAccount = accountRepository.findByUuid(input.getOriginAccountUUID()).orElseThrow(() -> new AccountNotFoundException(String.format("No target account with UUID %s found.", input.getOriginAccountUUID())));
        Account recipientAccount = accountRepository.findByUuid(input.getRecipientAccountUUID()).orElseThrow(() -> new AccountNotFoundException(String.format("No recipient account with UUID %s found.", input.getRecipientAccountUUID())));

        BigDecimal transferAmount = input.getAmount();

        String originCurrency = originAccount.getCurrency();
        String recipientCurrency = recipientAccount.getCurrency();

        if (!originCurrency.equals(recipientCurrency)) {
            transferAmount = conversionUseCase.execute(CurrencyConversionRequestDTO.builder()
                    .fromCurrency(originCurrency)
                    .toCurrency(recipientCurrency)
                    .amount(transferAmount)
                    .build());
        }

        balanceValidator.validate(BalanceValidationDTO.builder()
                    .account(originAccount)
                    .transferAmount(transferAmount)
                    .build())
                .ifPresent(errorMsg -> {
                    throw new InsufficientBalanceException(errorMsg);
                });

        originAccount.setBalance(originAccount.getBalance().subtract(transferAmount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(transferAmount));

        accountRepository.save(originAccount);
        accountRepository.save(recipientAccount);

        return MakeTransferResultDTO.builder()
                .originAccountBalance(originAccount.getBalance())
                .recipientAccountBalance(recipientAccount.getBalance())
                .build();
    }
}
