package com.somecompany.transferservice.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link com.somecompany.transferservice.model.Account}
 */
public record AccountDto(UUID uuid, UUID ownerId, String currency, BigDecimal balance) implements Serializable {
}