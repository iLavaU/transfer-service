package com.somecompany.transferservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "transfers")
public class  Transfer {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_generator")
    @SequenceGenerator(name = "transfer_generator", sequenceName = "transfers_SEQ", allocationSize = 1)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @Setter(AccessLevel.NONE)
    @Column(name="uuid", updatable = false, nullable = false, unique = true)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "recipient_account_id", referencedColumnName = "id", nullable = false)
    private Account recipientAccount;

    @OneToOne
    @JoinColumn(name = "origin_account_id", referencedColumnName = "id", nullable = false)
    private Account originAccount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @PrePersist
    protected void onCreate() {
        this.uuid = UUID.randomUUID();
    }
}
