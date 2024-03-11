package com.example.demo.service;


import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageGenerator {

    private final ReactiveKafkaService reactiveKafkaService;


    public MessageGenerator(ReactiveKafkaService reactiveKafkaService) {
        this.reactiveKafkaService = reactiveKafkaService;
    }

    public void sendMultipleMessagesToKafka(String topic, int numberOfMessages) {
        for (int i = 0; i < numberOfMessages; i++) {
            String key = UUID.randomUUID().toString();
            String message = "Message " + i;
            reactiveKafkaService.sendMessage(topic,key, message)
                    .subscribe(); // Subscribe to the Mono to trigger the sending of the message
        }
    }
}

