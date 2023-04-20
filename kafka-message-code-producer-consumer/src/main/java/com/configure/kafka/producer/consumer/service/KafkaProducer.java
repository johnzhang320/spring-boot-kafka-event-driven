package com.configure.kafka.producer.consumer.service;

import com.configure.kafka.producer.consumer.config.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    @Bean
    public NewTopic  createNewTopic () {
        return TopicBuilder.name(Constants.TOPIC_NAME).build();
    }

    private final KafkaTemplate<String, String > kafkaTemplate;

    public void sendMessage(String message) throws IOException {
        final Predicate<String> valueNotNullOrEmpty
                = e -> e != null && !e.isEmpty() && e.trim().length()>1;

        Path path = Paths.get("src/main/resume.txt");
       List<String> words= Files.lines(path)
               .flatMap(line -> Arrays.stream(line.trim().split("\\s")))
               .filter(valueNotNullOrEmpty)
               .map(word->word.replaceAll("[^a-zA-Z]","").toLowerCase().trim())
                .collect(Collectors.toList());

        StringBuffer totalMsg= new StringBuffer();
        totalMsg.append(message+"\n");
        words.forEach(x->totalMsg.append(x+" "));
        log.info("Sending message {} ",totalMsg);
        kafkaTemplate.send("MySpringTopic", totalMsg.toString());
    }
}
