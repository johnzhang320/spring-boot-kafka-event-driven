package com.configure.kafka.producer.consumer.service;

import com.configure.kafka.producer.consumer.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics= Constants.TOPIC_NAME, groupId=Constants.GROUP_ID_CONFIG)
    public void kafkaListener(String message) {

        log.info("Consumer get data: {} ", message);
    }

}
