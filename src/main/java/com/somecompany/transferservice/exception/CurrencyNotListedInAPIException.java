package com.somecompany.transferservice.exception;

public class CurrencyNotListedInAPIException extends RuntimeException{
    public CurrencyNotListedInAPIException(String currency) {
        super(String.format("Currency %s not listed in API.", currency));
    }
}
