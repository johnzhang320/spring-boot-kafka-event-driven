package com.configure.kafka.producer.consumer.controller;

import com.configure.kafka.producer.consumer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class MessagerController {

    private final KafkaProducer kafkaProducer;

    //http://localhost:8094/api/v1/kafka/publish?message=hello world
    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message) throws IOException {

        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok(message);
    }

}
