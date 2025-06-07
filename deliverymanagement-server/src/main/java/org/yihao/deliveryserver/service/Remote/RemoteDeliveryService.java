package org.yihao.deliveryserver.service.Remote;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.yihao.shared.DTOS.DeliveryDTO;
import org.yihao.shared.DTOS.DeliveryStatusChangeRequest;
import org.yihao.shared.DTOS.DriverDTO;
import org.yihao.shared.DTOS.DriverStatusChangeRequest;

@FeignClient("driver-server")
public interface RemoteDeliveryService {
    @PostMapping("admin/delivery")
    ResponseEntity<DeliveryDTO> addDelivery(
            @RequestHeader("role") String role, @RequestBody DeliveryDTO deliveryDTO
    );


    @GetMapping("admin/drivers/available")
    ResponseEntity<DriverDTO> getAvailableDriver(@RequestHeader("role") String role);

    @PutMapping("admin/drivers/status")
    ResponseEntity<DriverDTO> updateDriverStatus(
            @RequestHeader("role") String role,
            @RequestBody DriverStatusChangeRequest request
    );

    @PutMapping("driver/delivery")
    ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @RequestHeader("role") String role,
            @RequestHeader("tableId") Long driverId,
            @Valid @RequestBody DeliveryStatusChangeRequest request
    );

    @PutMapping("driver/profile/statusrefresh")
    ResponseEntity<DriverDTO> driverStatusRefresh(
            @RequestHeader("role") String role, @RequestHeader("tableId") Long driverId
    );
}
