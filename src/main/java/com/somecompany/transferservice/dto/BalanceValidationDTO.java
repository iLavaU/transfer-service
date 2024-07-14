package com.somecompany.transferservice.dto;

import com.somecompany.transferservice.model.Account;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceValidationDTO {
    private Account account;
    private BigDecimal transferAmount;
}
