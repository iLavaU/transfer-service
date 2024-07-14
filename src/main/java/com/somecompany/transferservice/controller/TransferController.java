package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.MakeTransferRequestDTO;
import com.somecompany.transferservice.dto.MakeTransferResultDTO;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/transfer")//TODO: complete mapping.
public class TransferController {

    private final MakeTransferUseCase makeTransferUseCase;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<MakeTransferResultDTO> transfer(@Valid MakeTransferRequestDTO makeTransferRequestDTO) {
        return ResponseEntity.accepted().body(makeTransferUseCase.execute(makeTransferRequestDTO));
    }
}
