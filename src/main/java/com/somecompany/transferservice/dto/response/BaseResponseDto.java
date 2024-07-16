package com.somecompany.transferservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BaseResponseDto<T> implements Serializable {
    private String message = "Success when processing request.";
    private T data;

    public BaseResponseDto(T data) {
        this.data = data;
    }
}
