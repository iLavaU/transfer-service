package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import com.somecompany.transferservice.service.impl.ScheduleTransferUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {


    MockMvc mockMvc;
    TransferController transferController;

    @Mock
    MakeTransferUseCase uc;
    @Mock
    ScheduleTransferUseCase scheduleTransferUc;
    TestDataUtil testDataUtil;

    @BeforeEach
    void setUp() {
        testDataUtil = new TestDataUtil();
        transferController = new TransferController(uc, scheduleTransferUc);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test
    void makeTransferSuccess() throws Exception {
        String requestBody = Files.readString(Path.of("src/test/resources/requests/make-transfer-request-good.json"), StandardCharsets.UTF_8);
        String responseBody = Files.readString(Path.of("src/test/resources/responses/make-transfer-response-success.json"), StandardCharsets.UTF_8);

        when(uc.execute(argThat(input ->
                    input.getInOriginCurrency() &&
                    input.getAmount().equals(BigDecimal.ONE))))
                .thenReturn(new MakeTransferResultDto(BigDecimal.valueOf(998), BigDecimal.valueOf(1844.4916)));
        this.mockMvc.perform(post("/transfer/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void makeTransferBadReq() throws Exception {
        String requestBody = Files.readString(Path.of("src/test/resources/requests/make-transfer-request-bad.json"), StandardCharsets.UTF_8);

        this.mockMvc.perform(post("/transfer/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
