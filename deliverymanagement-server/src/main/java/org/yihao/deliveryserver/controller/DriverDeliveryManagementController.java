package org.yihao.deliveryserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.deliveryserver.Exception.APIException;
import org.yihao.deliveryserver.service.DriverDeliveryManagementService;
import org.yihao.shared.DTOS.DeliveryDTO;
import org.yihao.shared.DTOS.DeliveryStatusChangeRequest;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/driver/delivery")
public class DriverDeliveryManagementController {
    private DriverDeliveryManagementService driverDeliveryManagementService;

    public DriverDeliveryManagementController(DriverDeliveryManagementService driverDeliveryManagementService) {
        this.driverDeliveryManagementService = driverDeliveryManagementService;
    }

    @PutMapping
    public ResponseEntity<DeliveryDTO> changeDeliveryStatus(
            @RequestHeader("role") String role,
            @RequestHeader("tableId") Long driverId,
            @RequestBody DeliveryStatusChangeRequest request) {
        if(!role.equals(Role.DRIVER.name())) {
            throw new APIException("Only Drivers Can Change his/her Delivery Status");
        }
        DeliveryDTO deliveryDTO = driverDeliveryManagementService.changeDriverStatus(role,driverId,request);
        return ResponseEntity.ok(deliveryDTO);
    }
}
