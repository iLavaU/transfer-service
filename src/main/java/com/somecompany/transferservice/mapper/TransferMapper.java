package com.somecompany.transferservice.mapper;

import com.somecompany.transferservice.dto.TransferDTO;
import com.somecompany.transferservice.model.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransferMapper {

    Transfer toTransfer(TransferDTO transferDTO);
    TransferDTO toTransferDTO(Transfer transfer);
}
