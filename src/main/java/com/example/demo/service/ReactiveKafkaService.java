package com.example.demo.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;


    public ReactiveKafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> sendMessage(String topic, String key, String message) {
        return Mono.fromRunnable(() -> kafkaTemplate.send(topic, key, message))
                .then();
    }
}
