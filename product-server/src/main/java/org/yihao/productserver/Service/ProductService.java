package org.yihao.productserver.Service;

import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO);

    ProductResponse getAllProducts(Long productId, String productName, Long supplierId, ProductPhase productPhase, ProductCategory productCategory, Long factoryId, Long warehouseId, Integer pageNumber, Integer pageSize, String sortBy, boolean desc);

    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO deleteProductById(Long productId);

    ProductDTO getProductById(Long productId);

}
