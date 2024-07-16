package com.somecompany.transferservice.exception;

import org.springframework.http.HttpStatusCode;

public class OerApiLatestException extends RuntimeException {

    public OerApiLatestException(HttpStatusCode statusCode) {
        super(String.format("Error when retrieving latest rates. Status is : %s", statusCode));
    }
}
