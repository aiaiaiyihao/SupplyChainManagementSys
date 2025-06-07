package org.yihao.ordermanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.yihao.ordermanagementserver.DTO.ProductDTO;

@FeignClient("product-server")
public interface RemoteProductService {
    @GetMapping("admin/products/{productId}")
    ResponseEntity<ProductDTO> getProduct(@RequestHeader("role") String role, @PathVariable Long productId);
}
