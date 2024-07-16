package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.dto.response.AccountCreationDto;
import com.somecompany.transferservice.mapper.AccountMapperImpl;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.impl.CreateAccountUseCase;
import com.somecompany.transferservice.service.impl.GetOwnerByUuidUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    TestDataUtil testDataUtil = new TestDataUtil();

    @BeforeEach
    public void setUp(){
        this.createAccountUC = new CreateAccountUseCase(getOwnerByUuidUC, accountMapper, accountRepository);
    }

    @Test
    public void testSuccessfulAccountCreation() {
        when(getOwnerByUuidUC.execute(testDataUtil.OWNER_1_UUID)).thenReturn(testDataUtil.TEST_OWNER_1);
        when(accountRepository.save(argThat(account ->
                        account.getCurrency().equals("USD") &&
                        account.getOwner().equals(testDataUtil.TEST_OWNER_1))))
                .thenReturn(testDataUtil.TEST_ACCOUNT_OWNER_1);

        createAccountUC.execute(new AccountCreationDto(testDataUtil.OWNER_1_UUID, "USD"));
    }
}
