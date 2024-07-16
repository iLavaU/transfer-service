package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.dto.CurrencyConversionResultDto;
import com.somecompany.transferservice.dto.BalanceValidationDto;
import com.somecompany.transferservice.dto.request.CurrencyConversionRequestDto;
import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.exception.InsufficientBalanceException;
import com.somecompany.transferservice.mapper.TransferMapper;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Transfer;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.repository.TransferRepository;
import com.somecompany.transferservice.service.UseCase;
import com.somecompany.transferservice.validator.impl.BalanceValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MakeTransferUseCase implements UseCase<MakeTransferRequestDto, MakeTransferResultDto> {

    private final CurrencyConversionUseCase conversionUC;
    private final GetAccountByUuidUseCase getAccountByUuidUC;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private AccountRepository accountRepository;
    private final BalanceValidator balanceValidator;

    @Override
    @Transactional
    public MakeTransferResultDto execute(MakeTransferRequestDto input) {

        Account originAccount = getAccountByUuidUC.execute(input.getOriginAccountUUID());
        Account recipientAccount = getAccountByUuidUC.execute(input.getRecipientAccountUUID());

        String originCurrency = originAccount.getCurrency();
        String recipientCurrency = recipientAccount.getCurrency();

        boolean differentCurrencies = !originCurrency.equals(recipientCurrency);
        BigDecimal deductFromOriginAcc;
        BigDecimal creditToRecipientAcc;
        if (differentCurrencies) {
            CurrencyConversionResultDto convRes = conversionUC.execute(CurrencyConversionRequestDto.builder()
                    .fromCurrency(originCurrency)
                    .toCurrency(recipientCurrency)
                    .inOriginCurrency(input.getInOriginCurrency())
                    .amount(input.getAmount())
                    .build());
            deductFromOriginAcc = convRes.deductFromOriginAcc();
            creditToRecipientAcc = convRes.creditToRecipientAcc();
        } else {
            deductFromOriginAcc = input.getAmount();
            creditToRecipientAcc = input.getAmount();
        }

        balanceValidator.validate(BalanceValidationDto.builder()
                    .account(originAccount)
                    .transferAmount(deductFromOriginAcc)
                    .build())
                .ifPresent(errorMsg -> {
                    throw new InsufficientBalanceException(errorMsg);
                });

        originAccount.setBalance(originAccount.getBalance().subtract(deductFromOriginAcc));
        recipientAccount.setBalance(recipientAccount.getBalance().add(creditToRecipientAcc));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Transfer transfer = transferMapper.makeTransferDtoToTransfer(recipientAccount, originAccount);

        accountRepository.save(originAccount);
        accountRepository.save(recipientAccount);
        transferRepository.save(transfer);

        return new MakeTransferResultDto(originAccount.getBalance(), recipientAccount.getBalance());
    }
}
