# spring-boot-kafka-event-driven
## spring boot kafka json and string producer and consumer 
  We created two projects, instead of application.properties or application.yml configuration, we use code to fillful configuration
  Main reason one: AS CS version of Intellij community edition, it does not support properties file "auto complete" feature, code config does
  second reason: consumer JsonDeSerialize needs ConcurrentKafkaListenerContainerFactory to config listener directly consumes json object not a 
  message string, so far we can not find this configure by yml or properties 

## Start Zookeeper and Kafka
   download kafka_2.12-3.3.1 
   
## Produce and consume Json Object

In configure-kafka-producer-consumer prject


