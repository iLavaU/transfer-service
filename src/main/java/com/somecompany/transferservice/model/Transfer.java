package com.somecompany.transferservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Column(name = "amount_transferred_from_origin_acc",  nullable = false)
    private BigDecimal amountTransferredFromOriginAcc;

    @Column(name = "amount_transferred_to_recipient_acc",  nullable = false)
    private BigDecimal amountTransferredToRecipientAcc;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id", referencedColumnName = "uuid", nullable = false)
    private Account recipientAccount;

    @ManyToOne
    @JoinColumn(name = "origin_account_id", referencedColumnName = "uuid", nullable = false)
    private Account originAccount;

    @Column(name = "transfer_date")
    private LocalDateTime transferDate;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @PrePersist
    protected void onCreate() {
        this.uuid = UUID.randomUUID();
    }
}
