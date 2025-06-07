package org.yihao.deliveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DeliveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServerApplication.class, args);
    }

}
