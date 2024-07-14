package com.somecompany.transferservice.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MakeTransferRequestDTO {
    private UUID originAccountUUID;
    private UUID recipientAccountUUID;
    @Positive
    private BigDecimal amount;
}
