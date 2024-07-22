package com.somecompany.transferservice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PendingTransferDto implements Serializable {
    private UUID originAccount;
    private UUID recipientAccount;
    private BigDecimal amount;
}