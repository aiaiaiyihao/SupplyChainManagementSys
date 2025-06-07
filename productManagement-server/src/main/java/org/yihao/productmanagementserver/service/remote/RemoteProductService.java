package org.yihao.productmanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yihao.shared.DTOS.ProductDTO;

@FeignClient("product-server")
public interface RemoteProductService {
    @PostMapping("/supplier/products")
    ResponseEntity<ProductDTO> createProduct(
        @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
        @RequestBody ProductDTO productDTO
    );

    @PutMapping("admin/products/{productId}")
    ResponseEntity<ProductDTO> updateProductById(
            @RequestHeader("role") String role,
            @PathVariable("productId") Long productId,
            @RequestBody ProductDTO productDTO
    );
}
