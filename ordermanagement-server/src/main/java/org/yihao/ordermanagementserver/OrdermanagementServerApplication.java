package org.yihao.ordermanagementserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrdermanagementServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdermanagementServerApplication.class, args);
    }

}
