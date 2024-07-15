package com.somecompany.transferservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.transferservice.dto.transfer.OpenExchangeRatesLatestDto;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;

public class TestDataUtil {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Owner TEST_OWNER_1 = createTestOwner1();
    public Owner TEST_OWNER_2 = createTestOwner2();
    public UUID OWNER_1_UUID = UUID.fromString("e2280ab6-c1e1-450b-8d53-c7698d12abfb");
    public UUID OWNER_2_UUID = UUID.fromString("6bb44b8a-0192-41a3-bea8-b44de3a09939");

    public Account TEST_ACCOUNT_OWNER_1 = createTestAccountOwner1();
    public UUID TEST_ACCOUNT_OWNER_1_UUID = UUID.fromString("beb15528-4348-4201-aca5-f2e2c9a09bfb");

    public Account TEST_ACCOUNT_OWNER_2 = createTestAccountOwner2();

    public  UUID TEST_ACCOUNT_OWNER_2_UUID = UUID.fromString("7722b9dc-d734-4ce4-b13d-9ed7b1fbda57");

    public OpenExchangeRatesLatestDto OER_RESULT_DTO = createOerResultDto();
    public BigDecimal AMOUNT = BigDecimal.valueOf(1);
    public BigDecimal EURToUSD = OER_RESULT_DTO.rates().get("EUR");
    public BigDecimal GBPToUSD = OER_RESULT_DTO.rates().get("GBP");
    public BigDecimal GBPToEUR_AMOUNT = AMOUNT.divide(GBPToUSD, MathContext.DECIMAL64).multiply(EURToUSD);
    public BigDecimal EURToGBP_AMOUNT = AMOUNT.divide(EURToUSD, MathContext.DECIMAL64).multiply(GBPToUSD);

    private Owner createTestOwner1() {
        Owner owner = new Owner();
        owner.setUuid(OWNER_1_UUID);
        owner.setEmail("julian.alvarez@test.com");
        owner.setFirstName("Julian");
        owner.setLastName("Alvarez");
        return owner;
    }

    private Owner createTestOwner2() {
        Owner owner = new Owner();
        owner.setEmail("paul.mccartney@test.com");
        owner.setUuid(OWNER_2_UUID);
        owner.setFirstName("Paul");
        owner.setLastName("McCartney");
        return owner;
    }

    private Account createTestAccountOwner1() {
        Account account = new Account();
        account.setOwner(TEST_OWNER_1);
        account.setUuid(TEST_ACCOUNT_OWNER_1_UUID);
        account.setCurrency("EUR");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private Account createTestAccountOwner2() {
        Account account = new Account();
        account.setOwner(TEST_OWNER_2);
        account.setUuid(TEST_ACCOUNT_OWNER_2_UUID);
        account.setCurrency("GBP");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private OpenExchangeRatesLatestDto createOerResultDto()  {
        File file = new File("src/test/resources/oerlatest-response.json");
        try {
            return OBJECT_MAPPER.readValue(file, OpenExchangeRatesLatestDto.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
