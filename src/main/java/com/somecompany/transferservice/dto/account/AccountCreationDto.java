package com.somecompany.transferservice.dto.account;

import com.somecompany.transferservice.annotation.ValidCurrency;
import com.somecompany.transferservice.validator.ValidationOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@GroupSequence({AccountCreationDto.class, ValidationOrder.AccountChecks.class})
public record AccountCreationDto(
        @NotNull
        UUID ownerUuid,
        @NotBlank
        @ValidCurrency(groups = ValidationOrder.AccountChecks.class)
        String currency
) {}
