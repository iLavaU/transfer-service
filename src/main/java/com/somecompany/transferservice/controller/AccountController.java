package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.response.AccountCreationDto;
import com.somecompany.transferservice.dto.request.AccountDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.mapper.AccountMapper;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.service.impl.CreateAccountUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/account")
public class AccountController {

    private CreateAccountUseCase createAccountUseCase;
    private AccountMapper accountMapper;

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDto<AccountDto>> createAccount(@RequestBody @Validated AccountCreationDto dto) {
        Account acc = createAccountUseCase.execute(dto);
        return new ResponseEntity<>(new BaseResponseDto<>(accountMapper.accountToAccountDto(acc)), HttpStatus.CREATED);
    }
}
