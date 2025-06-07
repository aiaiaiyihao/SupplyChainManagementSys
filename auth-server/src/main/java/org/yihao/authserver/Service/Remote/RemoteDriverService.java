package org.yihao.authserver.Service.Remote;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.shared.DTOS.DriverDTO;

@FeignClient("driver-server")
public interface RemoteDriverService {
    @PostMapping("/admin/drivers")
    ResponseEntity<DriverDTO> createDriver(
            @RequestHeader("role") String role, @Valid @RequestBody DriverDTO driverDTO);
}
