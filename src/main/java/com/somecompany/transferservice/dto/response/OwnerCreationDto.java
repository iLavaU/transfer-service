package com.somecompany.transferservice.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerCreationDto(
        @Size(min = 2, max = 60)
        String firstName,
        @Size(min = 2, max = 60)
        @NotNull
        String lastName,
        @Email
        @NotNull
        @NotBlank
        String email) {
}
