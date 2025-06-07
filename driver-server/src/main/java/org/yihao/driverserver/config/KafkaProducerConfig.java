package org.yihao.driverserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

import static org.yihao.shared.config.AppConstants.TOPIC_NAME_DELIVERY_DELAY;

public class KafkaProducerConfig {
    private static final Short REPLICATION_FACTOR = (short) 1;

//    private static final String TOPIC_NAME_DELIVERY_DELAY = "deliveryDelay";

    private static final Integer PARTITION_NUMBER_PRODUCT = 2;


    /**
     * Tell Spring topic we want to create topic
     *
     */


    @Bean
    public NewTopic createDeliverySupplierTopic() {
        return new NewTopic(TOPIC_NAME_DELIVERY_DELAY, PARTITION_NUMBER_PRODUCT, REPLICATION_FACTOR);
    }


}
