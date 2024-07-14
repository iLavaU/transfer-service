package com.somecompany.transferservice.dto.transfer;

import com.somecompany.transferservice.model.Account;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceValidationDto {
    private Account account;
    private BigDecimal transferAmount;
}
