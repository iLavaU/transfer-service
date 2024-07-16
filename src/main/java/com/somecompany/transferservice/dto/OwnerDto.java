package com.somecompany.transferservice.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.somecompany.transferservice.model.Owner}
 */
public record OwnerDto(UUID uuid, String firstName, String lastName, String email) implements Serializable {
}