package org.yihao.productmanagementserver.service;

import org.yihao.shared.DTOS.ProductDTO;

public interface SupplierProductManService {
    ProductDTO createProduct(Long supplierId, ProductDTO productDTO, String role);
}
