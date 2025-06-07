package org.yihao.supplierserver.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;
import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Service.SupplierService;
@Slf4j
@RestController
@RequestMapping("supplier/profiles")
public class SupplierProfileController {
    private SupplierService supplierService;

    public SupplierProfileController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<SupplierDTO> getSupplierProfileBySupplierId(
            @RequestHeader("role") String role,@RequestHeader("tableId") Long supplierId) {
        if(!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("ONLY SUPPLIER CAN GET ITS PROFILE");
        }
        SupplierDTO supplierDTO = supplierService.getSupplierProfileBySupplierId(supplierId);
        return new ResponseEntity<>(supplierDTO, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplierProfile(
            @RequestHeader("role") String role,
            @Valid @RequestBody SupplierDTO supplierDTO){
        if(!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("ONLY SUPPLIER CAN CREATE ITS PROFILE");
        }
        SupplierDTO supplierDTOResponse = supplierService.createSupplierProfile(supplierDTO);
        return new ResponseEntity<>(supplierDTOResponse, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SupplierDTO> updateSupplierProfileBySupplierId(
            @RequestHeader("role") String role,
            @RequestBody SupplierDTO supplierDTO,
            @RequestHeader("tableId") Long supplierId) {
        if(!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("ONLY SUPPLIER CAN UPDATE ITS PROFILE");
        }
        SupplierDTO supplierDTOUpdated = supplierService.updateSupplierProfileBySupplierId(supplierDTO,supplierId);
        return new ResponseEntity<>(supplierDTOUpdated, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SupplierDTO> deleteSupplierProfileBySupplierId(
            @RequestHeader("role") String role,
            @RequestHeader("tableId") Long supplierId) {
        if(!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("ONLY SUPPLIER CAN DELETE ITS PROFILE");
        }
        SupplierDTO supplierDTOdeleted = supplierService.deleteSupplierProfileBySupplierId(supplierId);
        return new ResponseEntity<>(supplierDTOdeleted, HttpStatus.OK);
    }
}
