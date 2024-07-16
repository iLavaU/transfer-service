package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.mapper.AccountMapperImpl;
import com.somecompany.transferservice.service.impl.CreateAccountUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    MockMvc mockMvc;
    AccountController accountController;

    @Mock
    CreateAccountUseCase uc;
    TestDataUtil testDataUtil = new TestDataUtil();

    @BeforeEach
    void setUp() {
        accountController = new AccountController(uc, new AccountMapperImpl());
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void accountCreationSuccess() throws Exception {
        String requestBody = Files.readString(Path.of("src/test/resources/requests/account-creation-good-request.json"), StandardCharsets.UTF_8);
        String responseBody = Files.readString(Path.of("src/test/resources/responses/account-creation-success-response.json"), StandardCharsets.UTF_8);

        when(uc.execute(argThat(creationDto ->
                    creationDto.currency().equals("USD") &&
                    creationDto.ownerUuid().equals(UUID.fromString("beb15528-4348-4201-aca5-f2e2c9a09bfb")))))
                .thenReturn(testDataUtil.TEST_ACCOUNT_OWNER_1);
        this.mockMvc.perform(post("/account/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    void accountCreationFailedBadCurrency() throws Exception {
        String requestBody = Files.readString(Path.of("src/test/resources/requests/account-creation-bad-currency-request.json"), StandardCharsets.UTF_8);

        this.mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void accountCreationFailedBadUuid() throws Exception {
        String requestBody = Files.readString(Path.of("src/test/resources/requests/account-creation-bad-uuid-request.json"), StandardCharsets.UTF_8);

        this.mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
