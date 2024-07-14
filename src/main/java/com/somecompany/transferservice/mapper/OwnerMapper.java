package com.somecompany.transferservice.mapper;

import com.somecompany.transferservice.dto.owner.OwnerCreationDto;
import com.somecompany.transferservice.dto.owner.OwnerDto;
import com.somecompany.transferservice.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerMapper {
    Owner toOwner(OwnerCreationDto owner);
    OwnerDto toOwnerDto(Owner owner);
}
