package com.somecompany.transferservice.repository;

import com.somecompany.transferservice.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
