package com.somecompany.transferservice.dto.response;

import lombok.Data;

@Data
public class ServerErrorResponseDto {
    private String message = "Unable to process request due to internal error. Please try again later.";
    private String detail;
}
