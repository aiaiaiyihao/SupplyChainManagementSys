package org.yihao.webhookserver.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/*No, you do not need to define /actuator/busrefresh manually in config-server.*/
@FeignClient(name="config-server")
public interface ConfigClient {
    @PostMapping("/actuator/busrefresh")
    void triggerBusRefresh();
}
