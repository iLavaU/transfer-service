package com.somecompany.transferservice.mapper;

import com.somecompany.transferservice.dto.response.AccountCreationDto;
import com.somecompany.transferservice.dto.request.AccountDto;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    @Mapping(target = "ownerId", source = "owner.uuid")
    AccountDto accountToAccountDto(Account account);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "balance", constant = "0")
    Account accountCreationDtoToAccount(AccountCreationDto creationDto, Owner owner);
}
