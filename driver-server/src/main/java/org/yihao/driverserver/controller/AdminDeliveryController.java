package org.yihao.driverserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.config.AppConstants;
import org.yihao.driverserver.service.ManagerDeliveryService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {
    private final ManagerDeliveryService managerDeliveryService;

    public AdminDeliveryController(ManagerDeliveryService managerDeliveryService) {
        this.managerDeliveryService = managerDeliveryService;
    }

    @GetMapping
    public ResponseEntity<DeliveryResponse> getAllDeliveries(
            @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check all deliveries");
        }
        DeliveryResponse deliveryResponse = managerDeliveryService.getAllDeliveries(pageNumber,pageSize);
        return ResponseEntity.ok(deliveryResponse);
    }



    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@RequestHeader("role") String role,@PathVariable Long deliveryId) {
        if(!role.equals(Role.MANAGER.name())) throw new APIException("Only Manager can check delivery By Id");
        DeliveryDTO deliveryDTO = managerDeliveryService.getDeliveryById(deliveryId);
        return ResponseEntity.ok(deliveryDTO);
    }



    @GetMapping("driver/{driverId}")
    public ResponseEntity<DeliveryResponse> getDeliveriesByDriverId(
            @RequestHeader("role") String role,
            @PathVariable Long driverId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize

    ) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check all deliveries");
        }
        DeliveryResponse driverResponse = managerDeliveryService.getDeliveryByDriverId(driverId,pageNumber,pageSize);
        return ResponseEntity.ok(driverResponse);
    }



    @PostMapping
    public ResponseEntity<DeliveryDTO> addDelivery(@RequestHeader("role") String role,@RequestBody DeliveryDTO deliveryDTO) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can create delivery");
        }
        DeliveryDTO deliveryDTOCreated = managerDeliveryService.createDelivery(deliveryDTO);
        return ResponseEntity.ok(deliveryDTOCreated);
    }

    /*delivery status can also be updated*/
    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> updateDelivery(@RequestHeader("role") String role,@PathVariable Long deliveryId, @RequestBody DeliveryDTO deliveryDTO) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can update delivery By Id");
        }
        DeliveryDTO deliveryDTOUpdated = managerDeliveryService.updateDeliveryById(deliveryId,deliveryDTO);
        return ResponseEntity.ok(deliveryDTOUpdated);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> deleteDelivery(
            @RequestHeader("role") String role,@PathVariable Long deliveryId) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can delete delivery By Id");
        }
        DeliveryDTO deliveryDTODeleted = managerDeliveryService.deleteDeliveryById(deliveryId);
        return ResponseEntity.ok(deliveryDTODeleted);
    }


}
