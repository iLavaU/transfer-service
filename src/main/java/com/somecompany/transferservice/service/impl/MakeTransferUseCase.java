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
import com.somecompany.transferservice.model.TransferStatus;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.repository.TransferRepository;
import com.somecompany.transferservice.service.UseCase;
import com.somecompany.transferservice.validator.impl.BalanceValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class MakeTransferUseCase implements UseCase<MakeTransferRequestDto, MakeTransferResultDto> {

    private final CurrencyConversionUseCase conversionUC;
    private final GetAccountByUuidUseCase getAccountByUuidUC;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final BalanceValidator balanceValidator;

    @Override
    @Transactional
    public MakeTransferResultDto execute(MakeTransferRequestDto input) {

        Accounts accounts = getOriginAndDestinationAccounts(input);
        Amounts amounts = getAmounts(input, accounts.originAccount().getCurrency(), accounts.recipientAccount().getCurrency());

        Transfer transfer = transferMapper.makeTransferDtoToTransfer(accounts.recipientAccount(), accounts.originAccount(),
                null, null);
        log.info("Validating account balance...");
        balanceValidator.validate(BalanceValidationDto.builder()
                    .account(accounts.originAccount())
                    .transferAmount(amounts.deductFromOriginAcc())
                    .build())
                .ifPresent(errorMsg -> {
                    transfer.setStatus(TransferStatus.FAILED);
                    transferRepository.save(transfer);
                    throw new InsufficientBalanceException(errorMsg);
                });

        TransferStatus transferStatus = TransferStatus.SUCCESSFUL;
        try {
            accounts.originAccount().setBalance(accounts.originAccount().getBalance().subtract(amounts.deductFromOriginAcc()));
            accounts.recipientAccount().setBalance(accounts.recipientAccount().getBalance().add(amounts.creditToRecipientAcc()));
            accountRepository.save(accounts.originAccount());
            accountRepository.save(accounts.recipientAccount());
            transfer.setAmountTransferredFromOriginAcc(amounts.deductFromOriginAcc);
            transfer.setAmountTransferredToRecipientAcc(amounts.creditToRecipientAcc);
        } catch (Exception e) {
            log.error("Unable to perform transfer.", e);
            transferStatus = TransferStatus.FAILED;
            throw e;
        } finally {
            transfer.setStatus(transferStatus);
            transferRepository.save(transfer);
        }

        return new MakeTransferResultDto(accounts.originAccount().getBalance(), accounts.recipientAccount().getBalance());
    }

    private Accounts getOriginAndDestinationAccounts(MakeTransferRequestDto input) {
        Account originAccount = getAccountByUuidUC.execute(input.getOriginAccountUUID());
        Account recipientAccount = getAccountByUuidUC.execute(input.getRecipientAccountUUID());
        return new Accounts(originAccount, recipientAccount);
    }

    private Amounts getAmounts(MakeTransferRequestDto input, String originCurrency, String recipientCurrency) {
        boolean differentCurrencies = !originCurrency.equals(recipientCurrency);
        BigDecimal deductFromOriginAcc;
        BigDecimal creditToRecipientAcc;
        if (differentCurrencies) {
            log.info("Different currencies detected.");
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
        return new Amounts(deductFromOriginAcc, creditToRecipientAcc);
    }

    private record Accounts(Account originAccount, Account recipientAccount) {}
    private record Amounts(BigDecimal deductFromOriginAcc, BigDecimal creditToRecipientAcc) {}
}
