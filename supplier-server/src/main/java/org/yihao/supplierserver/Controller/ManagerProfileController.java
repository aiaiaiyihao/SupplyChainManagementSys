package org.yihao.supplierserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;
import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Service.ManagerService;
import org.yihao.supplierserver.Service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("admin/suppliers")
public class ManagerProfileController {
    private final ManagerService managerService;
    private final SupplierService supplierService;

    public ManagerProfileController(ManagerService managerService, SupplierService supplierService) {
        this.managerService = managerService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<SupplierPageResponse> getAllSuppliers(
            @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Check All Supplier...");
        }
        SupplierPageResponse supplierDTOS = managerService.getAllSuppliers(pageNumber,pageSize);
        return new ResponseEntity<>(supplierDTOS, HttpStatus.OK);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDTO> getSupplierById(
            @RequestHeader("role") String role,
            @PathVariable Long supplierId) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Check Supplier...");
        }
        SupplierDTO supplierDTO = managerService.getSupplierById(supplierId);
        return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
    }



    @PutMapping("{supplierId}/offboard")
    public ResponseEntity<SupplierStatusDTO> offBoardSupplierBySupplierId(
            @RequestHeader("role") String role,
            @PathVariable("supplierId") Long supplierId) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Offboard Supplier...");
        }
        SupplierStatusDTO supplierStatusDTO = managerService.offBoardSupplierBySupplierId(supplierId);
        return new ResponseEntity<>(supplierStatusDTO, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(
            @RequestHeader("role") String role,
            @RequestBody SupplierDTO supplierDTO) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Create Supplier...");
        }
        SupplierDTO supplierProfile = supplierService.createSupplierProfile(supplierDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierProfile);
    }

    @DeleteMapping("{supplierId}")
    public ResponseEntity<SupplierDTO> deleteSupplierBySupplierId(
            @RequestHeader("role") String role,
            @PathVariable("supplierId") Long supplierId) {
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Delete Supplier Profile...");
        }
        SupplierDTO supplierDTO = supplierService.deleteSupplierProfileBySupplierId(supplierId);
        return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
    }
}
