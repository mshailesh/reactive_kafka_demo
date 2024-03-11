package com.example.demo.listner;



import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;

import java.time.Duration;

//@Component
class ReactiveKafkaListner {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveKafkaListner.class);

    private int messageCount = 0;
    private static final int ACK_AFTER_COUNT = 100;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private KafkaReceiver<String, String> kafkaReceiver;


    public ReactiveKafkaListner(KafkaTemplate<String, String> kafkaTemplate, KafkaReceiver<String, String> kafkaReceiver) {
        this.kafkaTemplate = kafkaTemplate;

        this.kafkaReceiver = kafkaReceiver;
    }

    // Reactive Kafka Listener
    @KafkaListener(topics = "kafka.reactive.demo", groupId = "reactive-java", containerFactory = "kafkaListenerContainerFactory")
    public Disposable listenForKafkaMessages(String message, Acknowledgment acknowledgment) {
      //  logger.info("Received message (reactive): {}", message);
       // System.out.println("Received message (reactive): {}" + message);
        return Flux.just(message)
                .doOnNext(this::processMessage)
                .subscribeOn(Schedulers.parallel()) // Ensure parallel processing
                .subscribe(record -> {
                    // Increment message count for each received message
                    messageCount++;

                    // Check if it's time to acknowledge messages
                    if (messageCount % ACK_AFTER_COUNT == 0) {
                        acknowledgment.acknowledge();
                    }
                });
    }

    private void processMessage(String message) {
        // Process the message (e.g., store in database, perform business logic)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Processing message (reactive): {}" ,message);
    }

  @EventListener
    public Disposable startKafkaConsumer(KafkaReceiver<String, String> kafkaReceiver) {
        return kafkaReceiver.receive()
                .doOnError(error -> logger.error("Error receiving event, will retry", error))
                .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofMinutes(1)))
                .doOnNext(record -> logger.debug("Received event: key {}", record.key()))
                .flatMap(record -> Flux.just(record)
                        .subscribeOn(Schedulers.parallel()) // Process each record on a parallel scheduler
                        .flatMap(this::handleEvent)
                        .doOnNext(event -> record.receiverOffset().acknowledge()) // Acknowledge each record
                )
                .subscribe();
    }

    @PostConstruct
    public void initialize() {
        // Start Kafka consumer when the bean is initialized
        // You might want to inject KafkaReceiver or any other required beans here
        // For simplicity, assuming KafkaReceiver is autowired or injected
        // You can also perform other initialization tasks here if needed
        // This method will be called once after all dependencies are injected
        startKafkaConsumer(kafkaReceiver);
    }
    public Mono<ReceiverRecord<String, String>> handleEvent(ReceiverRecord<String, String> record) {
        // Implement your event handling logic here
        // This method should return a Mono of the same record after processing
        // You can perform any necessary processing such as deserialization, validation, database operations, etc.

        // For example, you can log the received
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Handling event: {}",  record.value());

        // You can also perform any error handling if needed

        // Then return the record wrapped in a Mono
        return Mono.just(record);
    }

    private void acknowledgeMessages(ReceiverOffset receiverOffset) {
        // Acknowledge messages
        receiverOffset.acknowledge();

        // Reset message count
        messageCount = 0;
    }


    // Other bean declarations and configurations...
}

