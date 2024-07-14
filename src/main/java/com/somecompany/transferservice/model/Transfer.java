package com.somecompany.transferservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "transfers")
public class Transfer {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;



    @OneToOne
    @JoinColumn(name = "destination_account_id", referencedColumnName = "id")
    private Account destinationAccount;

    @OneToOne
    @JoinColumn(name = "origin_account_id", referencedColumnName = "id")
    private Account originAccount;

    @Column(name = "transfer_date")
    private LocalDateTime transferDate;
}
