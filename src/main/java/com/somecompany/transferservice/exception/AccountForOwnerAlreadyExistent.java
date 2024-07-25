package com.somecompany.transferservice.exception;

import java.util.UUID;

public class AccountForOwnerAlreadyExistent extends RuntimeException {
    public AccountForOwnerAlreadyExistent(UUID ownerUuid) {
        super(String.format("Account for owner %s already exists", ownerUuid));
    }
}
