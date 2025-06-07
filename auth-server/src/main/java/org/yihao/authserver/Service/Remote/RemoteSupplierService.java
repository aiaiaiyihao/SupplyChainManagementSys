package org.yihao.authserver.Service.Remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.shared.DTOS.SupplierDTO;

@FeignClient("supplier-server")
public interface RemoteSupplierService {
    @PostMapping("admin/suppliers")
    ResponseEntity<SupplierDTO> createSupplier(
            @RequestHeader("role") String role,
            @RequestBody SupplierDTO supplierDTO
    );
}
