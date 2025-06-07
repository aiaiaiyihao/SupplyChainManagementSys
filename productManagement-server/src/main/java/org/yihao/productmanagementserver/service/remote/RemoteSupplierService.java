package org.yihao.productmanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.shared.DTOS.FactoryDTO;

@FeignClient("supplier-server")
public interface RemoteSupplierService {

    @GetMapping("supplier/factories/{factoryId}")
    ResponseEntity<FactoryDTO> findSupplierFactoryByFactoryId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @PathVariable Long factoryId
    );
}
