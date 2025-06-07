package org.yihao.inventoryserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yihao.inventoryserver.Exception.APIException;
import org.yihao.inventoryserver.service.ManagerInOutBoundService;
import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryMovementResponse;
import org.yihao.shared.ENUMS.MovementType;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

@Controller
@RequestMapping("/admin/inout")
public class ManagerInOutBoundController {
    private ManagerInOutBoundService managerInOutBoundService;

    public ManagerInOutBoundController(ManagerInOutBoundService managerInOutBoundService) {
        this.managerInOutBoundService = managerInOutBoundService;
    }

    @GetMapping
    public ResponseEntity<InventoryMovementResponse> getAllInOutBound(
            @RequestHeader("role") String role,
            @RequestParam(required = false) MovementType movementType,
            @RequestParam(required = false) Long inventoryId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Boolean createdToday,
            @RequestParam(defaultValue = "movementId") String sortBy,
            @RequestParam(defaultValue = "false") boolean desc,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can check In/out Bound record");
        }
        InventoryMovementResponse response = managerInOutBoundService.getAllInoutBound(movementType,inventoryId,
                productId,productName, warehouseId,createdToday,sortBy,desc,pageNumber,pageSize);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{inventoryMovementId}")
    public ResponseEntity<InventoryMovementDTO> updateInOutBoundQuantity(
            @RequestHeader("role") String role,
            @PathVariable Long inventoryMovementId ,
            @RequestBody InventoryMovementDTO inventoryMovementDTO){
        if(!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can update In/out Bound quantity record");
        }
        InventoryMovementDTO inventoryMovementDTOUpdate = managerInOutBoundService.updateInOutBoundById(inventoryMovementId,inventoryMovementDTO);
        return ResponseEntity.ok(inventoryMovementDTOUpdate);
    }


}

