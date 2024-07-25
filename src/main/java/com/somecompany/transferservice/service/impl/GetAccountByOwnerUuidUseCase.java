package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetAccountByOwnerUuidUseCase implements UseCase<UUID, Optional<Account>> {

    private final AccountRepository accountRepo;

    @Override
    public Optional<Account> execute(UUID input) {
        return accountRepo.findByOwnerUuid(input);
    }
}
