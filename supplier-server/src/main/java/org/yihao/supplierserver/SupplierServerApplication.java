package org.yihao.supplierserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SupplierServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierServerApplication.class, args);
    }

}
