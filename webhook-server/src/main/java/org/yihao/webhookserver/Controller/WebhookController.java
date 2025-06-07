package org.yihao.webhookserver.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yihao.webhookserver.external.ConfigClient;

@RestController
public class WebhookController {
    private final ConfigClient configClient;

    @Autowired
    public WebhookController(ConfigClient configClient) {
        this.configClient = configClient;
    }

    @PostMapping("/webhook")
    public String receiveWebhook(@RequestBody String payload) {
        System.out.println("Received GitHub webhook: " + payload);
        /*No, you do not need to define /actuator/busrefresh manually in config-server.*/
        configClient.triggerBusRefresh();
        System.out.println("sent webhook");
        return "success";
    }
}
