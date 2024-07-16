package com.somecompany.transferservice.dto.response;

import lombok.Data;

@Data
public class ClientErrorResponseDto {
    private String message = "Unable to process request. Please verify request parameters and body.";
    private String detail;
}
