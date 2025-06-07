package org.yihao.facilityserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.facilityserver.Exception.APIException;
import org.yihao.facilityserver.Service.ManagerWarehouseService;
import org.yihao.facilityserver.model.Warehouse;
import org.yihao.shared.DTOS.WarehouseDTO;
import org.yihao.shared.DTOS.WarehouseResponse;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.ENUMS.WarehouseStatus;
import org.yihao.shared.config.AppConstants;

@RestController
@RequestMapping("/admin/warehouse")
public class AdminWarehouseController {
    private final ManagerWarehouseService managerWarehouseService;

    public AdminWarehouseController(ManagerWarehouseService managerWarehouseService) {
        this.managerWarehouseService = managerWarehouseService;
    }

    @GetMapping
    public ResponseEntity<WarehouseResponse> getAllWarehouses(
            @RequestHeader("role") String role,
            @RequestParam(required = false) WarehouseStatus status,
            @RequestParam(required = false) Long zipCode,
            @RequestParam(defaultValue = "warehouseName") String sortBy,
            @RequestParam(defaultValue = "false") boolean desc,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can check warehouses...");
        }
        WarehouseResponse warehouseDTOS = managerWarehouseService.getAllWarehouses(pageNumber,pageSize,status,zipCode,sortBy,desc);
        return new ResponseEntity<>(warehouseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(
            @RequestHeader("role") String role, @PathVariable Long warehouseId) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can check warehouse by ID");
        }
        WarehouseDTO warehouseDTO = managerWarehouseService.getWarehouseById(warehouseId);
        return new ResponseEntity<>(warehouseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> addWarehouse(
            @RequestHeader("role") String role,
            @RequestBody WarehouseDTO warehouseDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can add warehouse");
        }
        WarehouseDTO warehouseDTOCreated = managerWarehouseService.createWarehouse(warehouseDTO);
        return new ResponseEntity<>(warehouseDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> updateWarehouseProfile(
            @RequestHeader("role") String role,
            @PathVariable Long warehouseId,
            @RequestBody WarehouseDTO warehouseDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can update warehouse By ID");
        }
        WarehouseDTO warehouseDTOUpdated = managerWarehouseService.updateWarehouse(warehouseId, warehouseDTO);
        return new ResponseEntity<>(warehouseDTOUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> deleteWarehouse(
            @RequestHeader("role") String role, @PathVariable Long warehouseId) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can delete warehouse By ID");
        }
        WarehouseDTO warehouseDTOUpdated = managerWarehouseService.deleteWarehouse(warehouseId);
        return new ResponseEntity<>(warehouseDTOUpdated, HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<WarehouseDTO> changeWarehouseStatus(
            @RequestHeader("role") String role,
            @RequestBody WarehouseDTO warehouseDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only MANAGER can change warehouse Status");
        }
        WarehouseDTO warehouseDTOUpdated = managerWarehouseService.changeWarehouseStatus(warehouseDTO);
        return new ResponseEntity<>(warehouseDTOUpdated, HttpStatus.OK);
    }
}
