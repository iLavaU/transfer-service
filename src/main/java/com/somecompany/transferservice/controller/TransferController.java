package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import com.somecompany.transferservice.service.impl.ScheduleTransferUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

    private final MakeTransferUseCase makeTransferUseCase;
    private final ScheduleTransferUseCase scheduleTransferUC;

    @PostMapping(path = "v1",consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponseDto<MakeTransferResultDto>> transferV1(@RequestBody @Valid MakeTransferRequestDto dto) {
        log.info("Performing transfer in {} currency for amount {}", dto.getInOriginCurrency() ? "origin" : "destination", dto.getAmount());
        MakeTransferResultDto resultDto = makeTransferUseCase.execute(dto);
        log.info("Transfer successfully performed.");
        return ResponseEntity.ok().body(new BaseResponseDto<>("Transfer performed successfully.", resultDto));
    }

    @PostMapping(path = "v2",consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponseDto<String>> transferV2(@RequestBody @Valid MakeTransferRequestDto dto) {
        log.info("Performing transfer v2 in {} currency for amount {}", dto.getInOriginCurrency() ? "origin" : "destination", dto.getAmount());
        scheduleTransferUC.execute(dto);
        log.info("Transfer successfully scheduled.");
        return ResponseEntity.ok().body(new BaseResponseDto<>("Transfer successfully scheduled.",
                String.format("Scheduled transfer for recipient account and origin accounts: %s and %s.", dto.getRecipientAccountUUID(), dto.getOriginAccountUUID())));
    }
}
