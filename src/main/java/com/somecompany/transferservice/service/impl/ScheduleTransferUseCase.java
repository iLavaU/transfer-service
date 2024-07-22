package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.kafka.PendingTransferProducer;
import com.somecompany.transferservice.service.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleTransferUseCase implements UseCase<MakeTransferRequestDto, Void> {

    private final PendingTransferProducer producer;

    @Override
    public Void execute(MakeTransferRequestDto input) {
        producer.send(input);
        return null;
    }
}
