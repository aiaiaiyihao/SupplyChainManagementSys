package org.yihao.ordermanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yihao.shared.DTOS.SupplierDTO;

@FeignClient("supplier-server")
public interface RemoteSupplierService {
    @GetMapping("admin/suppliers/{supplierId}")
    ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long supplierId);
}
