package com.somecompany.transferservice.kafka;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.MakeTransferResultDto;
import com.somecompany.transferservice.service.impl.GetAccountByUuidUseCase;
import com.somecompany.transferservice.service.impl.MakeTransferUseCase;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class PendingTransferConsumer {

    private final MakeTransferUseCase makeTransferUC;
    private final GetAccountByUuidUseCase getAccountByUuidUC;

    @Transactional
    @KafkaListener(topics = "${kafka.producer.pending-transfer.topic}", groupId = "${kafka.consumer.pending-transfer.groupid}",
            containerFactory = "listenerContainerFactory")
    public void receivePendingTransferDto(MakeTransferRequestDto dto) {
        validateExistentAccounts(dto);

        MakeTransferResultDto resultDto = makeTransferUC.execute(dto);
        log.info("Finished processing pending transfer: {}", resultDto);
    }

    protected void validateExistentAccounts(MakeTransferRequestDto dto) {
        getAccountByUuidUC.execute(dto.getOriginAccountUUID());
        getAccountByUuidUC.execute(dto.getRecipientAccountUUID());
    }
}
