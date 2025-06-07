package org.yihao.deliveryserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    private static final Integer PARTITION_NUMBER = 5;
    private static final Short REPLICATION_FACTOR = (short) 1;

    private static final String TOPIC_NAME = "JTSP-demo-3";

    private static final String TOPIC_NAME_DELIVERY_SUPPLIER = "deliverySupplier";
    private static final String TOPIC_NAME_DELIVERY_DRIVER = "deliveryDriver";

    private static final Integer PARTITION_NUMBER_PRODUCT = 2;


    /**
     * Tell Spring topic we want to create topic
     *
     */


    @Bean
    public NewTopic createDeliverySupplierTopic() {
        return new NewTopic(TOPIC_NAME_DELIVERY_SUPPLIER, PARTITION_NUMBER_PRODUCT, REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic createDeliveryDriverTopic() {
        return new NewTopic(TOPIC_NAME_DELIVERY_DRIVER, PARTITION_NUMBER_PRODUCT, REPLICATION_FACTOR);
    }

}
