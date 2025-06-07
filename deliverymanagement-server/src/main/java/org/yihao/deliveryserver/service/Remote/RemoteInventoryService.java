package org.yihao.deliveryserver.service.Remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yihao.shared.DTOS.InventoryDTO;
import org.yihao.shared.DTOS.InventoryMovementDTO;

@FeignClient("inventory-server")
public interface RemoteInventoryService {
    @PutMapping("admin/inventory")
    ResponseEntity<InventoryDTO> updateInventoryByMovement(@RequestBody InventoryMovementDTO inventoryMovementDTO);
}
