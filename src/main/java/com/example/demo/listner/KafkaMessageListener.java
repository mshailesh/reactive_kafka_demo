package com.example.demo.listner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class KafkaMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);
    private static final int ACK_AFTER_COUNT = 100;
    private int messageCount = 0;

    // Traditional Kafka Listener
    @KafkaListener(topics = "kafka.reactive.demo", groupId = "traditional-group1", containerFactory =  "kafkaListenerContainerFactory")
    public void listenForKafkaMessages(String message, Acknowledgment acknowledgment) {
        // Process the received message
        processMessage(message);
        // Increment message count and check for acknowledgment
        if (++messageCount % ACK_AFTER_COUNT == 0) {
            acknowledgment.acknowledge(); // Acknowledge messages after every 100th message
        }
    }

    @KafkaListener(topics = "kafka.reactive.demo", groupId = "exec-serv-group", containerFactory =  "kafkaListenerContainerFactory")
    public void listenForKafkaMessages1(String message, Acknowledgment acknowledgment) {
        // Process the received message

        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.submit(()-> this.processMessage1(message));


        // Increment message count and check for acknowledgment
        if (++messageCount % ACK_AFTER_COUNT == 0) {
            acknowledgment.acknowledge(); // Acknowledge messages after every 100th message
        }
    }

    private void processMessage(String message) {
        // Process the message (e.g., store in database, perform business logic)
        try {
            Thread.sleep(1000);
            logger.info("Processing message (traditional): {}" ,message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    private void processMessage1(String message) {
        // Process the message (e.g., store in database, perform business logic)
        try {
            Thread.sleep(1000);
            logger.info("Processing message (executors): {}" ,message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}