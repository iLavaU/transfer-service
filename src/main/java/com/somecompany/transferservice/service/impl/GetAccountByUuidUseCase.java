package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.exception.AccountNotFoundException;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetAccountByUuidUseCase implements UseCase<UUID, Account> {

    private AccountRepository accountRepository;

    @Override
    public Account execute(UUID input) {
        return accountRepository.findByUuid(input).orElseThrow(() ->
                new AccountNotFoundException(String.format("No account with UUID %s found.",input))
        );
    }
}
