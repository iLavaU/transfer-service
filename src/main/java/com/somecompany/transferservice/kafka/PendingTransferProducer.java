package com.somecompany.transferservice.kafka;

import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PendingTransferProducer {
    private final String topic;
    private final KafkaTemplate<String, MakeTransferRequestDto> kafkaTemplate;

    public PendingTransferProducer(
            @Value("${kafka.producer.pending-transfer.topic}") String topic,
            @Qualifier("pendingTransferKafkaTemplate") KafkaTemplate<String, MakeTransferRequestDto> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MakeTransferRequestDto dto) {
        log.info("Sending message pending transfer message to topic.");
        kafkaTemplate.send(topic, dto);
    }
}
