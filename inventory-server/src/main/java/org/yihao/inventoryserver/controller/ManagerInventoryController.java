package org.yihao.inventoryserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yihao.inventoryserver.Exception.APIException;
import org.yihao.inventoryserver.service.ManagerInventoryService;
import org.yihao.shared.DTOS.InventoryDTO;
import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryQueryDTO;
import org.yihao.shared.DTOS.InventoryResponse;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

@Controller
@RequestMapping("admin/inventory")
public class ManagerInventoryController {
    private final ManagerInventoryService managerInventoryService;

    public ManagerInventoryController(ManagerInventoryService managerInventoryService) {
        this.managerInventoryService = managerInventoryService;
    }

    @GetMapping("/inventories")
    public ResponseEntity<InventoryResponse> getAllInventoriesPage(
            @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only manager can view inventories");
        }
        InventoryResponse response = managerInventoryService.getAllInventoriesPage(pageNumber,pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> getInventoryById(
            @RequestHeader("role") String role,
            @PathVariable("inventoryId") Long inventoryId){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only manager can view inventory by InventoryId");
        }
        InventoryDTO inventoryDTO = managerInventoryService.getInventoryById(inventoryId);
        return ResponseEntity.ok(inventoryDTO);
    }

    //增加条件筛选
    //get all by Product Id


    //get all by warehouse Id

    @GetMapping
    public ResponseEntity<InventoryDTO> getInventoryByProductIdAndWarehouseId(
            @RequestHeader("role") String role,
            @RequestBody InventoryQueryDTO queryDTO){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only manager can view inventory by ProductId and WarehouseId");
        }
        InventoryDTO inventoryDTO = managerInventoryService.getInventoryByProductIdAndWarehouseId(queryDTO);
        return ResponseEntity.ok(inventoryDTO);
    }



    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(
            @RequestHeader("role") String role,
            @RequestBody InventoryDTO inventoryDTO){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only manager can create inventory");
        }
        InventoryDTO inventoryDTOCreated = managerInventoryService.createInventory(inventoryDTO);
        return new ResponseEntity<>(inventoryDTOCreated, HttpStatus.CREATED);
    }


    @PutMapping("{inventoryId}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @RequestHeader("role") String role,
            @PathVariable("inventoryId") Long inventoryId
            ,@RequestBody InventoryDTO inventoryDTO){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only manager can update inventory By InventoryId");
        }
        InventoryDTO inventoryDTOUpdate = managerInventoryService.updateInventoryById(inventoryId,inventoryDTO);
        return new ResponseEntity<>(inventoryDTOUpdate, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<InventoryDTO> updateInventoryByMovement(
            @RequestHeader("role") String role,
            @RequestBody InventoryMovementDTO inventoryMovementDTO){
        if(!role.equals(Role.MANAGER.name())&&!role.equals(Role.DRIVER.name())){
            throw new APIException("Only MANAGER/DRIVER can update inventory By In/Out Movement");
        }
        InventoryDTO inventoryDTOUpdate = managerInventoryService.updateInventoryByMovement(inventoryMovementDTO);
        return new ResponseEntity<>(inventoryDTOUpdate, HttpStatus.OK);
    }

}
