package com.somecompany.transferservice.exception;

import com.somecompany.transferservice.dto.response.ClientErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            AccountNotFoundException.class,
            OwnerNotFoundException.class
    })
    public ResponseEntity<ClientErrorResponseDto> handleAccountNotFoundException(Exception ex, WebRequest request) {
        ClientErrorResponseDto clientErrorResponseDto = new ClientErrorResponseDto();
        clientErrorResponseDto.setDetail(ex.getMessage());
        return new ResponseEntity<>(clientErrorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            CurrencyNotListedInAPIException.class,
            InsufficientBalanceException.class
    })
    public ResponseEntity<ClientErrorResponseDto> handleInsufficientBalanceException(Exception ex, WebRequest request) {
        ClientErrorResponseDto clientErrorResponseDto = new ClientErrorResponseDto();
        clientErrorResponseDto.setDetail(ex.getMessage());
        return new ResponseEntity<>(clientErrorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = OerApiLatestException.class)
    public ResponseEntity<ClientErrorResponseDto> handleOerApiLatestException(OerApiLatestException ex, WebRequest request) {
        ClientErrorResponseDto clientErrorResponseDto = new ClientErrorResponseDto();
        clientErrorResponseDto.setMessage("Error calling Open Exchange Rates API. Please try again later.");
        clientErrorResponseDto.setDetail(ex.getMessage());
        return new ResponseEntity<>(clientErrorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ClientErrorResponseDto clientErrorResponseDto = new ClientErrorResponseDto();
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        clientErrorResponseDto.setDetail(errors.toString());
        return new ResponseEntity<>(clientErrorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
