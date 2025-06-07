package org.yihao.deliveryserver.service.Remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.shared.DTOS.Address;
import org.yihao.shared.DTOS.SupplierDTO;

@FeignClient("supplier-server")
public interface RemoteSupplierService {
    @GetMapping("/admin/suppliers/{supplierId}/factories/{factoryId}/address")
    ResponseEntity<Address> findAddressByFactoryId(
            @RequestHeader("role") String role,
            @PathVariable("supplierId") Long supplierId,
            @PathVariable("factoryId") Long factoryId
    );

    @GetMapping("admin/suppliers/{supplierId}")
    ResponseEntity<SupplierDTO> getSupplierById(
            @RequestHeader("role") String role,
            @PathVariable Long supplierId);
}
