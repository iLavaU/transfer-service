package com.somecompany.transferservice.repository;

import com.somecompany.transferservice.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUuid(UUID uuid);
}
