package com.somecompany.transferservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@Data
public class MakeTransferRequestDto implements Serializable {
    @NotNull
    private UUID originAccountUUID;
    @NotNull
    private UUID recipientAccountUUID;
    @NotNull
    @Positive
    private BigDecimal amount;
    private Boolean inOriginCurrency = true;
}
