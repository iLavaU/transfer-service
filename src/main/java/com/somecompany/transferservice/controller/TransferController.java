package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/transfer")//TODO: complete mapping.
public class TransferController {

    private final MakeTransferUseCase makeTransferUseCase;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponseDto<MakeTransferResultDto>> transfer(@RequestBody @Valid MakeTransferRequestDto makeTransferRequestDTO) {
        return ResponseEntity.ok().body(new BaseResponseDto<>("Transfer performed successfully.",makeTransferUseCase.execute(makeTransferRequestDTO)));
    }
}
