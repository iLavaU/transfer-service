package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.exception.OwnerNotFoundException;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.OwnerRepository;
import com.somecompany.transferservice.service.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOwnerByUuidUseCase implements UseCase<UUID, Owner> {

    private OwnerRepository ownerRepository;

    @Override
    public Owner execute(UUID input) {
        return ownerRepository.findByUuid(input).orElseThrow(() ->
                new OwnerNotFoundException(String.format("Owner with uuid %s not found.", input)));
    }
}
