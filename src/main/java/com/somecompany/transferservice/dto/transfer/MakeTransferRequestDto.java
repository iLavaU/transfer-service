package com.somecompany.transferservice.dto.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;


@Data
public class MakeTransferRequestDto {
    @NotNull
    private UUID originAccountUUID;
    @NotNull
    private UUID recipientAccountUUID;
    @Positive
    private BigDecimal amount;
    private Boolean inOriginCurrency = true;
}
