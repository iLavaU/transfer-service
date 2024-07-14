package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.dto.owner.OwnerCreationDto;
import com.somecompany.transferservice.mapper.OwnerMapper;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.OwnerRepository;
import com.somecompany.transferservice.service.UseCase;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateOwnerUseCase implements UseCase<OwnerCreationDto, Owner> {

    private OwnerRepository ownerRepository;
    private OwnerMapper ownerMapper;

    @Override
    @Transactional
    public Owner execute(OwnerCreationDto input) {
        Owner owner = ownerMapper.toOwner(input);
        return ownerRepository.save(owner);
    }
}
