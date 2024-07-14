package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.dto.account.AccountCreationDto;
import com.somecompany.transferservice.mapper.AccountMapper;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.UseCase;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAccountUseCase implements UseCase<AccountCreationDto, Account> {

    private GetOwnerByUuidUseCase getOwnerByUuidUC;
    private AccountMapper accountMapper;
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public Account execute(AccountCreationDto input) {
        Owner owner = getOwnerByUuidUC.execute(input.ownerUuid());
        Account account = accountMapper.accountCreationDtoToAccount(input, owner);
        return accountRepository.save(account);
    }
}
