package org.yihao.productserver.Service;

import org.springframework.web.multipart.MultipartFile;
import org.yihao.shared.DTOS.DownloadResponse;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.DTOS.ProductResponse;

public interface SupplierProductService {
    ProductDTO createProduct(Long supplierId, ProductDTO productDTO);

    ProductResponse getAllProductsBySupplierId(Long supplierId, Integer pageNumber, Integer pageSize);

    ProductDTO updateProductByProductId(Long supplierId, Long productId, ProductDTO productDTO);

    ProductDTO deleteSupplierProductByProductId(Long supplierId, Long productId);

    ProductDTO uploadImageByProductId(Long supplierId, Long productId, String email, MultipartFile file);

    DownloadResponse downloadImageByProductId(Long productId, Long supplierId);
}

