package org.yihao.driverserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.config.AppConstants;
import org.yihao.driverserver.service.DriverDeliveryService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/driver/delivery")
public class DriverDeliveryController {
    private final DriverDeliveryService driverDeliveryService;

    public DriverDeliveryController(DriverDeliveryService driverDeliveryService) {
        this.driverDeliveryService = driverDeliveryService;
    }

    @GetMapping
    public ResponseEntity<DeliveryResponse> getDeliveriesByDriverId(
            @RequestHeader("role") String role,
            @RequestHeader("tableId") Long driverId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if(!role.equals(Role.DRIVER.name())) throw new APIException("Only Drivers can check his/her own deliveries");
        DeliveryResponse driverResponse = driverDeliveryService.getDeliveryByDriverId(driverId,pageNumber,pageSize);
        return ResponseEntity.ok(driverResponse);
    }


    @PutMapping
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @RequestHeader("role") String role,
            @RequestHeader("tableId") Long driverId,
            @Valid @RequestBody DeliveryStatusChangeRequest request) {
        if(!role.equals(Role.DRIVER.name())) throw new APIException("Only Drivers can update his/her own delivery status");
        DeliveryDTO deliveryDTO = driverDeliveryService.updateDeliveryStatus(driverId,request);
        return ResponseEntity.ok(deliveryDTO);
    }
}
