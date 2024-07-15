package com.somecompany.transferservice.service;

import com.somecompany.transferservice.dto.account.AccountCreationDto;
import com.somecompany.transferservice.mapper.AccountMapperImpl;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.impl.CreateAccountUseCase;
import com.somecompany.transferservice.service.impl.GetOwnerByUuidUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.somecompany.transferservice.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    GetOwnerByUuidUseCase getOwnerByUuidUC;
    AccountMapperImpl accountMapper = new AccountMapperImpl();
    CreateAccountUseCase createAccountUC;

    @BeforeEach
    public void setUp(){
        this.createAccountUC = new CreateAccountUseCase(getOwnerByUuidUC, accountMapper, accountRepository);
    }

    @Test //TODO: complete.
    public void testSuccessfulAccountCreation() {
        when(getOwnerByUuidUC.execute(TEST_ACCOUNT_OWNER_1_UUID)).thenReturn(TEST_OWNER_1);
        when(accountRepository.save(argThat(account ->
                        account.getCurrency().equals("USD") &&
                        account.getOwner().equals(TEST_OWNER_1) &&
                        account.getUuid().equals(TEST_ACCOUNT_OWNER_1_UUID))))
                .thenReturn(TEST_ACCOUNT_OWNER_1);

        createAccountUC.execute(new AccountCreationDto(TEST_ACCOUNT_OWNER_1_UUID, "USD"));
    }
}
