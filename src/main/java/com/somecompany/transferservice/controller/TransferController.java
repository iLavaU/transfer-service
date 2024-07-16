package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
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

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponseDto<MakeTransferResultDto>> transfer(@RequestBody @Valid MakeTransferRequestDto dto) {
        log.info("Performing transfer in {} currency for amount {}", dto.getInOriginCurrency() ? "origin" : "destination", dto.getAmount());
        MakeTransferResultDto resultDto = makeTransferUseCase.execute(dto);
        log.info("Transfer successfully performed.");
        return ResponseEntity.ok().body(new BaseResponseDto<>("Transfer performed successfully.", resultDto));
    }
}
