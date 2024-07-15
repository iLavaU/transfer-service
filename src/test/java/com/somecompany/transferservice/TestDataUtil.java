package com.somecompany.transferservice;

import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;

import java.math.BigDecimal;
import java.util.UUID;

public class TestDataUtil {
    public static final Owner TEST_OWNER_1 = createTestOwner1();
    public static final Owner TEST_OWNER_2 = createTestOwner2();
    public static final UUID OWNER_1_UUID = UUID.fromString("e2280ab6-c1e1-450b-8d53-c7698d12abfb");
    public static final UUID OWNER_2_UUID = UUID.fromString("6bb44b8a-0192-41a3-bea8-b44de3a09939");
    public static final Account TEST_ACCOUNT_OWNER_1 = createTestAccountOwner1();
    public static final UUID TEST_ACCOUNT_OWNER_1_UUID = UUID.fromString("beb15528-4348-4201-aca5-f2e2c9a09bfb");
    private static Owner createTestOwner1() {
        Owner owner = new Owner();
        owner.setUuid(OWNER_1_UUID);
        owner.setEmail("john.doe@test.com");
        owner.setFirstName("John");
        owner.setLastName("Doe");
        return owner;
    }

    private static Owner createTestOwner2() {
        Owner owner = new Owner();
        owner.setEmail("paul.mccartney@test.com");
        owner.setUuid(OWNER_2_UUID);
        owner.setFirstName("Paul");
        owner.setLastName("McCartney");
        return owner;
    }
    private static Account createTestAccountOwner1() {
        Account account = new Account();
        account.setOwner(TEST_OWNER_1);
        account.setUuid(TEST_ACCOUNT_OWNER_1_UUID);
        account.setCurrency("USD");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }
}
