package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.exception.AccountNotFoundException;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.UseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetAccountByUuidUseCase implements UseCase<UUID, Account> {

    private AccountRepository accountRepository;

    @Override
    public Account execute(UUID input) {
        log.info("Retrieving account by UUID: {}", input);
        return accountRepository.findByUuid(input).orElseThrow(() ->
                new AccountNotFoundException(String.format("No account with UUID %s found.",input))
        );
    }
}
