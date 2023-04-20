# spring-boot-kafka-event-driven
## spring boot kafka json and string producer and consumer 
  We created two projects, instead of application.properties or application.yml configuration, we use code to fillful configuration
  Main reason one: AS CS version of Intellij community edition, it does not support properties file "auto complete" feature, code config does
  second reason: consumer JsonDeSerialize needs ConcurrentKafkaListenerContainerFactory to config listener directly consumes json object not a 
  message string, so far we can not find this configure by yml or properties 

## Start Zookeeper and Kafka
       download kafka_2.12-2.1.0.tgz from https://archive.apache.org/dist/kafka/2.1.0/kafka_2.12-2.1.0.tgz
       tar cvx kafka_2.12-2.1.0.tgz
       add $KAFKA_HOME point to your kafka installation directory 
       cd ./spring-boot-kafka-event-driven/kafka_start_stop
       chmod 755 *
       zookeeper_start.sh
       kafka_start.sh
       jps
       make sure following two processes running
       xxxx QuorumPeerMain
       xxxx Kafka

   
   
## Produce and consume Json Object


  In configure-kafka-producer-consumer prject, we try transfer Order class from producer to consumer in Json format
  
  
      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      @Builder
      @ToString
      public class Order {
          private String item;
          private String deliveryType;
          private Integer quantity;
      }
 
 
  As we know , KafkaTemplate works for Producer, so create KafkaTemplate bean by configure ProducerFactory for Kafka Bootstrap Server, 
  key StringSerializer and Value JsonSerializer 
  
        @Bean
        KafkaTemplate<String, Order> producerKafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
        @Bean
        public ProducerFactory <String, Order> producerFactory() {
            Map<String, Object> config = new HashMap<>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_NAME);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(config);
        }
  For the consumer JSON configuration factory, besides Kafka Bootstrap Server, key StringDeserializer and value JsonDeserilizer, we especially
  need configure trust the producer model package or model Object, here is Order class, then use ConcurrentKafkaListenerContainerFactory to load
  the consumerfactory 
  
        @Bean
        public ConsumerFactory<String, Order> consumerFactory() {
            Map<String, Object> config = new HashMap<>();
            config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_NAME);
            config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            // consumer must trust either Package or Model Object of producer, this is trouble me so long time, either one is OK !!!
            config.put(JsonDeserializer.TRUSTED_PACKAGES,"com.configure.kafka.producer.consumer.model.Order");
            return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(),new JsonDeserializer<>(Order.class));
        }
           /*
             KafkaTemplate only works for JSON producer configuration
             not for JSON consumer , consumer needs ConcurrentKafkaListenerContainerFactory
           */
        @Bean
        public ConcurrentKafkaListenerContainerFactory<String,Order> kafkaListenerContainerFactory () {
            ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }
      
  Create ProducerService , which uses KafkaTemplate and ProductRecord to send the topic and order object 
  
        @RequiredArgsConstructor
        @Service
        @Slf4j
        public class ProducerService {
            private final KafkaTemplate<String, Order> kafkaTemplate;
            public void sendOrder(Order order) {
                log.info("sending order:"+order.toString());
                kafkaTemplate.send(new ProducerRecord<>(TOPIC_NAME, order));
            }
        }
 ConsumerService directly listens the Order Object by @KafkaListener
 

        @Service
        @Slf4j
        public class ConsumerService {
            @KafkaListener(topics= Constants.TOPIC_NAME, groupId=Constants.GROUP_ID_CONFIG)
            public void kafkaListener(Order order) {
                log.info("Consumer received order: {} ", order.toString());
            }
        }
        
 ## Result Test 
   Send data from postman to producer service
   
   ![](producer_sending_an_order.png)
 
   Consumer listent the object of Order in Json format
   
   ![](consumer_listened_order_json_object.png)
   
   
 ## Produce and consume String 
   In kafka-message-code-producer-consumer, configuration is simple, consumerfactory does not need to load into ConcurrentKafkaListener
   because consumer default accept string, all the code in repository, we do not need to example 
   Here 
   
   
