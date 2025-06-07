package org.yihao.productmanagementserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProductManagementServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementServerApplication.class, args);
    }

}
