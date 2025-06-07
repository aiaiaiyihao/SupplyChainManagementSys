package org.yihao.ordermanagementserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    private static final Integer PARTITION_NUMBER = 5;
    private static final Short REPLICATION_FACTOR = (short) 1;

    private static final String TOPIC_NAME = "JTSP-demo-3";

    public static final String TOPIC_NAME_ORDER = "orders";
    public static final String TOPIC_NAME_ORDERAPPROVAL = "ordersApproval";

    private static final Integer PARTITION_NUMBER_ORDER = 2;


    /**
     * Tell Spring topic we want to create topic
     *
     */

//    @Bean
//    public NewTopic createTopic() {
//        return new NewTopic(TOPIC_NAME, PARTITION_NUMBER, REPLICATION_FACTOR);
//    }

    @Bean
    public NewTopic createOrderTopic() {
        return new NewTopic(TOPIC_NAME_ORDER, PARTITION_NUMBER_ORDER, REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic createOrderApprovalTopic() {
        return new NewTopic(TOPIC_NAME_ORDERAPPROVAL, PARTITION_NUMBER_ORDER, REPLICATION_FACTOR);
    }

}
