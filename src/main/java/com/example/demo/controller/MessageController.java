package com.example.demo.controller;

import com.example.demo.service.MessageGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageGenerator someOtherService;

    public MessageController(MessageGenerator someOtherService) {
        this.someOtherService = someOtherService;
    }

    @GetMapping("/send-messages")
    public String sendMessages(@RequestParam String topic, @RequestParam int numberOfMessages) {
        someOtherService.sendMultipleMessagesToKafka(topic, numberOfMessages);
        return "Messages sent successfully";
    }
}

