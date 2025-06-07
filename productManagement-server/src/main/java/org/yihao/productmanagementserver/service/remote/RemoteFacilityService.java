package org.yihao.productmanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.shared.DTOS.WarehouseDTO;

@FeignClient("facility-server")
public interface RemoteFacilityService {
    @GetMapping("admin/warehouse/{warehouseId}")
    ResponseEntity<WarehouseDTO> getWarehouseById(
            @RequestHeader("role") String role, @PathVariable Long warehouseId
    );
}
