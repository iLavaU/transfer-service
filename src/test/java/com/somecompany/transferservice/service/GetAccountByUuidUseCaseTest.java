package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.exception.AccountNotFoundException;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.impl.GetAccountByUuidUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAccountByUuidUseCaseTest {

    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    GetAccountByUuidUseCase getAccountByUuidUC;

    TestDataUtil testDataUtil;

    @BeforeEach
    void setUp() {
        testDataUtil = new TestDataUtil();
    }

    @Test
    void getAccountByUuidFound() {
        when(accountRepository.findByUuid(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID)).thenReturn(Optional.of(testDataUtil.TEST_ACCOUNT_OWNER_1));
        Account account = getAccountByUuidUC.execute(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID);
        assertEquals("EUR", account.getCurrency());
    }

    @Test
    void getAccountByUuidNotFound() {
        when(accountRepository.findByUuid(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID)).thenReturn(Optional.empty());
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> getAccountByUuidUC.execute(testDataUtil.TEST_ACCOUNT_OWNER_1_UUID));
        assertEquals(String.format("No account with UUID %s found.", testDataUtil.TEST_ACCOUNT_OWNER_1_UUID), ex.getMessage());
    }

}
