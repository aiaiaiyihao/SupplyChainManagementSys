package org.yihao.webhookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableFeignClients
@RefreshScope
public class WebhookServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebhookServerApplication.class);
    }

/*    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/webhook")
    public String receiveWebhook(@RequestBody String payload) {
*//*
        // Create HttpHeaders and set the media type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the desired media type here
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);
        // Make a POST request with the desired media type
        *//**//*It is used to trigger a refresh of the configuration across all instances in a distributed system.
        Works with Spring Cloud Bus, which connects nodes (instances)
        in a distributed system using a message broker (like RabbitMQ or Kafka).
        Instead of calling /actuator/refresh on every instance, you can call /actuator/busrefresh on just one,
        and Spring Cloud Bus will propagate the refresh signal to all instances.*//**//*
        String url = "http://localhost:8088/actuator/busrefresh"; // Replace with the actual endpoint URL
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        return "Webhook received";*//*
    }*/
}