package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.client.OpenExchangeRatesAPIClient;
import com.somecompany.transferservice.dto.transfer.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.transfer.MakeTransferResultDto;
import com.somecompany.transferservice.exception.InsufficientBalanceException;
import com.somecompany.transferservice.mapper.TransferMapperImpl;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.repository.TransferRepository;
import com.somecompany.transferservice.service.impl.CurrencyConversionUseCase;
import com.somecompany.transferservice.service.impl.GetAccountByUuidUseCase;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import com.somecompany.transferservice.validator.impl.BalanceValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.somecompany.transferservice.validator.impl.BalanceValidator.ERROR_MSG_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MakeTransferUseCaseTest {

    @Mock
    OpenExchangeRatesAPIClient oerClient;
    CurrencyConversionUseCase conversionUC;
    @Mock
    GetAccountByUuidUseCase getAccountByUuidUC;
    TransferMapperImpl transferMapper = new TransferMapperImpl();
    @Mock
    TransferRepository transferRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    BalanceValidator balanceValidator;
    MakeTransferUseCase makeTransferUseCase;

    TestDataUtil testDataUtil;

    @BeforeEach
    void setUp() {
        conversionUC = new CurrencyConversionUseCase(oerClient);
        makeTransferUseCase = new MakeTransferUseCase(conversionUC, getAccountByUuidUC, transferMapper, transferRepository, accountRepository, balanceValidator);
        testDataUtil = new TestDataUtil();
        when(getAccountByUuidUC.execute(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID)).thenReturn(testDataUtil.TEST_ACCOUNT_OWNER_1);
        when(getAccountByUuidUC.execute(testDataUtil.TEST_ACCOUNT_OWNER_2_UUID)).thenReturn(testDataUtil.TEST_ACCOUNT_OWNER_2);
    }

    @Test
    void makeTransferDiffCurrenciesInOriginCurrency() {
        testDataUtil.TEST_ACCOUNT_OWNER_1.setBalance(BigDecimal.valueOf(100));
        MakeTransferRequestDto requestDto = new MakeTransferRequestDto();
        requestDto.setOriginAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID);
        requestDto.setRecipientAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_2_UUID);
        requestDto.setAmount(testDataUtil.AMOUNT);

        when(oerClient.getLatestOpenExchangeRates()).thenReturn(testDataUtil.OER_RESULT_DTO);

        MakeTransferResultDto resultDto = makeTransferUseCase.execute(requestDto);
        assertEquals(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(1)), resultDto.originAccountBalance());
        assertEquals(BigDecimal.ZERO.add(testDataUtil.EURToGBP_AMOUNT), resultDto.recipientAccountBalance());
    }

    @Test
    void makeTransferDiffCurrenciesNotInOriginCurrency() {
        testDataUtil.TEST_ACCOUNT_OWNER_1.setBalance(BigDecimal.valueOf(100));
        MakeTransferRequestDto requestDto = new MakeTransferRequestDto();
        requestDto.setOriginAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID);
        requestDto.setRecipientAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_2_UUID);
        requestDto.setInOriginCurrency(false);
        requestDto.setAmount(testDataUtil.AMOUNT);

        when(oerClient.getLatestOpenExchangeRates()).thenReturn(testDataUtil.OER_RESULT_DTO);

        MakeTransferResultDto resultDto = makeTransferUseCase.execute(requestDto);
        assertEquals(BigDecimal.valueOf(100).subtract(testDataUtil.GBPToEUR_AMOUNT), resultDto.originAccountBalance());
        assertEquals(BigDecimal.ZERO.add(testDataUtil.AMOUNT), resultDto.recipientAccountBalance());
    }

    @Test
    void makeTransferSameCurrency() {
        testDataUtil.TEST_ACCOUNT_OWNER_1.setBalance(BigDecimal.valueOf(100));
        testDataUtil.TEST_ACCOUNT_OWNER_2.setCurrency("EUR");
        MakeTransferRequestDto requestDto = new MakeTransferRequestDto();
        requestDto.setOriginAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID);
        requestDto.setRecipientAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_2_UUID);
        requestDto.setAmount(testDataUtil.AMOUNT);

        MakeTransferResultDto resultDto = makeTransferUseCase.execute(requestDto);
        assertEquals(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(1)), resultDto.originAccountBalance());
        assertEquals(BigDecimal.ZERO.add(testDataUtil.AMOUNT), resultDto.recipientAccountBalance());
    }

    @Test
    void makeTransferNotEnoughBalance() {
        testDataUtil.TEST_ACCOUNT_OWNER_1.setBalance(BigDecimal.valueOf(0));
        MakeTransferRequestDto requestDto = new MakeTransferRequestDto();
        requestDto.setOriginAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID);
        requestDto.setRecipientAccountUUID(testDataUtil.TEST_ACCOUNT_OWNER_2_UUID);
        requestDto.setAmount(testDataUtil.AMOUNT);

        when(oerClient.getLatestOpenExchangeRates()).thenReturn(testDataUtil.OER_RESULT_DTO);
        when(balanceValidator.validate(argThat(input ->
                    input.getTransferAmount().equals(testDataUtil.AMOUNT) &&
                    input.getAccount().equals(testDataUtil.TEST_ACCOUNT_OWNER_1))))
                .thenReturn(Optional.of(String.format(ERROR_MSG_TEMPLATE, BigDecimal.valueOf(0), testDataUtil.AMOUNT)));

        InsufficientBalanceException balanceException = assertThrows(InsufficientBalanceException.class, () -> makeTransferUseCase.execute(requestDto));
        assertEquals(String.format(ERROR_MSG_TEMPLATE, BigDecimal.valueOf(0), testDataUtil.AMOUNT), balanceException.getMessage());
    }


}
