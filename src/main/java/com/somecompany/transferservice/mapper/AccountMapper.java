package com.somecompany.transferservice.mapper;

import com.somecompany.transferservice.dto.account.AccountCreationDto;
import com.somecompany.transferservice.dto.account.AccountDto;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    @Mapping(target = "ownerId", source = "owner.uuid")
    AccountDto accountToAccountDto(Account account);
    @Mapping(target = "balance", constant = "0")
    Account accountCreationDtoToAccount(AccountCreationDto accountCreationDto, Owner owner);
}
