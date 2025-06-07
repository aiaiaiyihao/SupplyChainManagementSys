package org.yihao.driverserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.config.AppConstants;
import org.yihao.shared.DTOS.*;
import org.yihao.driverserver.service.ManagerDriverService;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("admin/drivers")
public class AdminDriverController {
    private ManagerDriverService managerDriverService;

    public AdminDriverController(ManagerDriverService managerDriverService) {
        this.managerDriverService = managerDriverService;
    }

    //manager see all driver information
    @GetMapping
    public ResponseEntity<DriverResponse> getAlldrivers(
            @RequestHeader("role") String role,
            @RequestParam(name = "driverStatus", required = false) DriverStatus driverStatus,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "driverId") String sortBy,
            @RequestParam(name = "desc", defaultValue = "false") boolean desc
    ) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check all drivers");
        }
        DriverResponse driverDTOS = managerDriverService.getAllDrivers(driverStatus, pageNumber, pageSize, sortBy, desc);
        return new ResponseEntity<>(driverDTOS, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<DriverDTO> getAvailableDriver(@RequestHeader("role") String role) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check the most available driver");
        }
        DriverDTO driverResponse = managerDriverService.getTheMostAvailableDriver();
        return new ResponseEntity<>(driverResponse, HttpStatus.OK);
    }

    //driver see his own information/manager see a driver information
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getdriverById(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check driver by id");
        }
        DriverDTO driverDTO = managerDriverService.getDriverById(id);
        return new ResponseEntity<>(driverDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(
            @RequestHeader("role") String role, @Valid @RequestBody DriverDTO driverDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Create Driver...");
        }
        DriverDTO driverDTOCreated = managerDriverService.createDriver(driverDTO);
        return new ResponseEntity<>(driverDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<DriverDTO> updateDriverProfile(
            @RequestHeader("role") String role,
            @PathVariable Long id, @Valid @RequestBody DriverDTO driverDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Update Driver...");
        }
        DriverDTO driverDTOUpdated = managerDriverService.updateById(id, driverDTO);
        return new ResponseEntity<>(driverDTOUpdated, HttpStatus.OK);
    }

    //need to update driver status
    @PutMapping("/status")
    public ResponseEntity<DriverDTO> updateDriverStatus(
            @RequestHeader("role") String role,
            @RequestBody DriverStatusChangeRequest request) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can update driver status by id");
        }
        DriverDTO driverDTOUpdated = managerDriverService.updateDriverStatus(request);
        return new ResponseEntity<>(driverDTOUpdated, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DriverDTO> deleteDriver(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can create driver");
        }
        DriverDTO driverDTODeleted = managerDriverService.deleteById(id);
        return new ResponseEntity<>(driverDTODeleted, HttpStatus.OK);
    }

}
