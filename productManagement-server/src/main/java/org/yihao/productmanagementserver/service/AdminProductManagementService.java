package org.yihao.productmanagementserver.service;

import org.yihao.shared.DTOS.ProductDTO;

public interface AdminProductManagementService {
    ProductDTO updateProductWarehouseId(String role, Long productId, ProductDTO productDTO);

}
