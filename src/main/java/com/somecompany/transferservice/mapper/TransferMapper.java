package com.somecompany.transferservice.mapper;

import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = LocalDateTime.class)
public interface TransferMapper {
    @Mapping(target = "transferDate", expression = "java(LocalDateTime.now())")
    Transfer makeTransferDtoToTransfer(Account recipientAccount, Account originAccount);
}
