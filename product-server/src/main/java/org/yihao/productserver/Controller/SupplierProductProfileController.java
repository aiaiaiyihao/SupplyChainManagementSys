package org.yihao.productserver.Controller;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yihao.productserver.Config.AppConstants;
import org.yihao.productserver.Service.SupplierProductService;
import org.yihao.shared.DTOS.DownloadResponse;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.DTOS.ProductResponse;
import org.yihao.shared.ENUMS.Role;
import org.yihao.supplierserver.Exception.APIException;

@RestController
@RequestMapping("supplier/products")
public class SupplierProductProfileController {
    private final SupplierProductService supplierProductService;
    public SupplierProductProfileController(SupplierProductService supplierProductService) {
        this.supplierProductService = supplierProductService;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getSupplierAllProducts(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if(!role.equals(Role.SUPPLIER.name())){
        throw new APIException("ONLY SUPPLIER CAN CHECK ITS PRODUCT...");
    }
        ProductResponse productResponse = supplierProductService.getAllProductsBySupplierId(supplierId,pageNumber,pageSize);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @RequestBody ProductDTO productDTO){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN CREATE ITS PRODUCT...");
        }
        ProductDTO productDTOCreated = supplierProductService.createProduct(supplierId,productDTO);
        return new ResponseEntity<>(productDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductByProductId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @PathVariable Long productId, @RequestBody ProductDTO productDTO){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN UPDATE ITS PRODUCT...");
        }
        ProductDTO productDTOUpdated = supplierProductService.updateProductByProductId(supplierId,productId,productDTO);
        return new ResponseEntity<>(productDTOUpdated, HttpStatus.OK);
    }


    @PutMapping("/upload/{productId}")
    public ResponseEntity<ProductDTO> uploadProductImageByProductId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @PathVariable Long productId, @RequestHeader("email") String email,
            @RequestParam("file") MultipartFile file)   {
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN UPDATE ITS PRODUCT IMAGE...");
        }
        ProductDTO productDTOUpdated = supplierProductService.uploadImageByProductId(supplierId,productId,email,file);
        return new ResponseEntity<>(productDTOUpdated, HttpStatus.OK);
    }

    @GetMapping("/download/{productId}")
    public ResponseEntity<ByteArrayResource> downloadProductByProductId(
            @PathVariable Long productId,@RequestHeader("role")String role,
            @RequestHeader("tableId") Long supplierId
    ){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN DOWNLOAD ITS PRODUCT IMAGE...");
        }
        DownloadResponse response = supplierProductService.downloadImageByProductId(productId, supplierId);
        ByteArrayResource byteArrayResource = new ByteArrayResource(response.getData());
        return ResponseEntity.ok()
                .contentLength(response.getData().length)
                .header("Content-type", response.getContentType())
                .header("Content-disposition", "attachment; filename=\"" + response.getFileName() +"\"")
                .body(byteArrayResource);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteSupplierProductByProductId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @PathVariable Long productId){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN DELETE ITS PRODUCT...");
        }
        ProductDTO productDTO = supplierProductService.deleteSupplierProductByProductId(supplierId,productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

}
