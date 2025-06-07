package org.yihao.inventoryserver.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yihao.inventoryserver.service.ManagerInventoryService;
import org.yihao.shared.DTOS.InventoryDTO;
import org.yihao.shared.DTOS.InventoryMovementDTO;

//@Controller
//@RequestMapping("driver/inventory")
//public class DriverInventoryController {
//    private ManagerInventoryService managerInventoryService;
//    public DriverInventoryController(ManagerInventoryService managerInventoryService) {
//        this.managerInventoryService = managerInventoryService;
//    }
//    @PutMapping
//    public ResponseEntity<InventoryDTO> updateInventoryByMovement(@RequestBody InventoryMovementDTO inventoryMovementDTO){
//        InventoryDTO inventoryDTOUpdate = managerInventoryService.updateInventoryByMovement(inventoryMovementDTO);
//        return new ResponseEntity<>(inventoryDTOUpdate, HttpStatus.OK);
//    }
//}
