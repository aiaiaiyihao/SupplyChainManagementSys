package org.yihao.productmanagementserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.productmanagementserver.Exception.APIException;
import org.yihao.productmanagementserver.service.SupplierProductManService;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/supplier")
public class SupplierProductManagementController {
    private final SupplierProductManService supplierProductManService;
    public SupplierProductManagementController(SupplierProductManService supplierProductManService) {
        this.supplierProductManService = supplierProductManService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @RequestBody ProductDTO productDTO
    ){
        if (!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("Only Supplier Can Create Its Products");
        }
        ProductDTO productDTOCreated = supplierProductManService.createProduct(supplierId,productDTO,role);
        return new ResponseEntity<>(productDTOCreated, HttpStatus.CREATED);
    }
}
