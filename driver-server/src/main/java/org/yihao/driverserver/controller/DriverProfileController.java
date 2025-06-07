package org.yihao.driverserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.service.DriverDriverService;
import org.yihao.shared.DTOS.DriverDTO;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("driver/profile")
public class DriverProfileController {
    private DriverDriverService driverDriverService;

    public DriverProfileController(DriverDriverService driverDriverService) {
        this.driverDriverService = driverDriverService;
    }
    //after driver delivery finish, decide if update his/her status to IDLE
    @PutMapping("/statusrefresh")
    public ResponseEntity<DriverDTO> driverStatusRefresh(
            @RequestHeader("role") String role, @RequestHeader("tableId") Long driverId) {
        if(!role.equals(Role.DRIVER.name())){
            throw new APIException("Only drivers can update driver status");
        }
        DriverDTO driverDTO = driverDriverService.driverStatusRefresh(driverId);
        return ResponseEntity.ok(driverDTO);
    }

    @GetMapping
    public ResponseEntity<DriverDTO> getDriverProfile(
            @RequestHeader("role") String role, @RequestHeader("tableId") Long driverId) {
        if(!role.equals(Role.DRIVER.name())){
            throw new APIException("ONLY DRIVER CAN CHECK DRIVER PROFILE");
        }
        DriverDTO driverDTO = driverDriverService.findDriveById(driverId);
        return ResponseEntity.ok(driverDTO);
    }

    @PutMapping
    public ResponseEntity<DriverDTO> updateDriverProfile(
            @RequestHeader("role") String role, @RequestHeader("tableId") Long driverId
    , @RequestBody DriverDTO driverDTO) {
        if(!role.equals(Role.DRIVER.name())){
            throw new APIException("ONLY DRIVER CAN UPDATE HIS/HER DRIVER PROFILE...");
        }
        DriverDTO driverDTOUpdate = driverDriverService.updateDriveById(driverId,driverDTO);
        return ResponseEntity.ok(driverDTOUpdate);
    }
}
