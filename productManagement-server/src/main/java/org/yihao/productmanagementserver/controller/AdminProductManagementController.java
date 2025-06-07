package org.yihao.productmanagementserver.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.productmanagementserver.Exception.APIException;
import org.yihao.productmanagementserver.service.AdminProductManagementService;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/admin")
public class AdminProductManagementController {
    private final AdminProductManagementService adminProductManagementService;
    public AdminProductManagementController(AdminProductManagementService adminProductManagementService){
        this.adminProductManagementService = adminProductManagementService;
    }

    @PutMapping("/warehouse/{productId}")
    public ResponseEntity<ProductDTO> updateProductWarehouseId(
            @RequestHeader("role") String role, @RequestBody ProductDTO productDTO,
            @PathVariable Long productId

    ){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Update Product warehouseId");
        }
        ProductDTO productDTOUpdated = adminProductManagementService.updateProductWarehouseId(role,productId,productDTO);
        return ResponseEntity.ok(productDTOUpdated);
    }

}
