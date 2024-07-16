package com.somecompany.transferservice.controller;

import com.somecompany.transferservice.dto.response.OwnerCreationDto;
import com.somecompany.transferservice.dto.OwnerDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.mapper.OwnerMapper;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.service.impl.CreateOwnerUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/owner")
public class OwnerController {

    private CreateOwnerUseCase createOwnerUseCase;
    private OwnerMapper ownerMapper;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponseDto<OwnerDto>> createOwner(@RequestBody @Valid OwnerCreationDto dto) {
        Owner owner = createOwnerUseCase.execute(dto);
        return new ResponseEntity<>(new BaseResponseDto<>(ownerMapper.toOwnerDto(owner)), HttpStatus.CREATED);
    }
}
