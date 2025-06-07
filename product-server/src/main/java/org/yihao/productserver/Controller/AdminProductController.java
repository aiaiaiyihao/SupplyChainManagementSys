package org.yihao.productserver.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.productserver.Config.AppConstants;
import org.yihao.productserver.Exception.APIException;
import org.yihao.productserver.Service.ProductService;
import org.yihao.productserver.Service.SupplierProductService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.OrderStatus;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;
import org.yihao.shared.ENUMS.Role;

@Slf4j
@RestController
@RequestMapping("admin/products")
public class AdminProductController {
    private final ProductService productService;
    private final SupplierProductService supplierProductService;

    public AdminProductController(ProductService productService, SupplierProductService supplierProductService) {
        this.productService = productService;
        this.supplierProductService = supplierProductService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @RequestHeader("role") String role, @RequestBody ProductDTO productDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Add Product");
        }
        ProductDTO productDTOAdded = productService.addProduct(productDTO);
        return new ResponseEntity<>(productDTOAdded, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestHeader("role") String role,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false)ProductPhase productPhase,
            @RequestParam(required = false) ProductCategory productCategory,
            @RequestParam(required = false) Long factoryId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "false") boolean desc
    ) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Check All Products");
        }
        log.info(pageSize.toString());
        ProductResponse productResponse = productService.getAllProducts(productId,productName,supplierId,
                productPhase,productCategory,factoryId,warehouseId,pageNumber,pageSize,sortBy,desc);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("supplier/{supplierId}")
    public ResponseEntity<ProductResponse> getAllProductsBySupplierId(
            @PathVariable Long supplierId, @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new org.yihao.supplierserver.Exception.APIException("ONLY SUPPLIER CAN CHECK ITS PRODUCT...");
        }
        ProductResponse productResponse = supplierProductService.getAllProductsBySupplierId(supplierId, pageNumber, pageSize);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(
            @RequestHeader("role") String role, @PathVariable Long productId) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Check Product By ProductId");
        }
        ProductDTO productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(
            @RequestHeader("role") String role,
            @PathVariable("productId") Long productId
            , @RequestBody ProductDTO productDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Update Product By ProductId");
        }
        ProductDTO productDTOUpdated = productService.updateProductById(productId, productDTO);
        return ResponseEntity.ok(productDTOUpdated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteProductById(
            @RequestHeader("role") String role,
            @PathVariable("productId") Long productId) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager Can Delete Product By ProductId");
        }
        ProductDTO productDTODeleted = productService.deleteProductById(productId);
        return ResponseEntity.ok(productDTODeleted);
    }
}

