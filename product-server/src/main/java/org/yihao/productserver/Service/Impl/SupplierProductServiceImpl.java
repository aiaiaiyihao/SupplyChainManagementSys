package org.yihao.productserver.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yihao.productserver.Exception.APIException;
import org.yihao.productserver.Exception.ResourceNotFoundException;
import org.yihao.productserver.Repository.ProductRepository;
import org.yihao.productserver.Service.StorageService;
import org.yihao.productserver.Service.SupplierProductService;
import org.yihao.productserver.model.Product;
import org.yihao.shared.DTOS.DownloadResponse;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.DTOS.ProductResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class SupplierProductServiceImpl implements SupplierProductService {
    private ProductRepository productRepository;
    private StorageService storageService;
    private ModelMapper modelMapper;

    public SupplierProductServiceImpl(
            ProductRepository productRepository, ModelMapper modelMapper,
            StorageService storageService) {
        this.productRepository = productRepository;
        this.storageService = storageService;
        this.modelMapper = modelMapper;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO createProduct(Long supplierId, ProductDTO productDTO) {
        if (productRepository.existsProductByProductName(productDTO.getProductName())) {
            throw new APIException("PRODUCT NAME ALREADY EXISTS");
        }
        productDTO.setSupplierId(supplierId);
        Product product = modelMapper.map(productDTO, Product.class);
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProductsBySupplierId(Long supplierId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsInPage = productRepository.findProductsBySupplierId(supplierId, pageDetails);
        List<Product> products = productsInPage.getContent();
        if (products.isEmpty()) {
            throw new APIException("NO PRODUCTS FOUND WITH SUPPLIER ID: " + supplierId);
        }
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productsInPage.getNumber());
        productResponse.setPageSize(productsInPage.getSize());
        productResponse.setTotalPages(productsInPage.getTotalPages());
        productResponse.setTotalElements(productsInPage.getTotalElements());
        productResponse.setLastPage(productsInPage.isLast());
        return productResponse;
    }

    @Override
    @CachePut(value = "products", key = "#productId")
    public ProductDTO updateProductByProductId(Long supplierId, Long productId, ProductDTO productDTO) {
        log.info("Fetching product from DB with id {}", productId);
        Product productFound = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
        if (!productFound.getSupplierId().equals(supplierId)) {
            throw new APIException("Product does not belong to the given supplier");
        }
        if (productDTO.getProductName() != null) {
            productFound.setProductName(productDTO.getProductName());
        }
        if (productDTO.getSupplierId() != null) {
            productFound.setSupplierId(productDTO.getSupplierId());
        }
        if (productDTO.getWarehouseId() != null) {
            productFound.setWarehouseId(productDTO.getWarehouseId());
        }
        if(productDTO.getFactoryId() != null) {
            productFound.setFactoryId(productDTO.getFactoryId());
        }
        if (productDTO.getProductDescription() != null) {
            productFound.setProductDescription(productDTO.getProductDescription());
        }
        if (productDTO.getProductPrice() != null) {
            productFound.setProductPrice(productDTO.getProductPrice());
        }
        if (productDTO.getCurrency() != null) {
            productFound.setCurrency(productDTO.getCurrency());
        }
        if (productDTO.getProductCategory() != null) {
            productFound.setProductCategory(productDTO.getProductCategory());
        }
        if (productDTO.getProductPhase() != null) {
            productFound.setProductPhase(productDTO.getProductPhase());
        }
        if (productDTO.getImageUrl() != null) {
            productFound.setImageUrl(productDTO.getImageUrl());
        }
        Product updatedProduct = productRepository.save(productFound);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    @CacheEvict(value = "products", key = "#productId")
    public ProductDTO deleteSupplierProductByProductId(Long supplierId, Long productId) {
        log.info("Fetching product from DB with id {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
        if (!product.getSupplierId().equals(supplierId)) {
            throw new APIException("Product does not belong to the given supplier");
        }
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    @CachePut(value = "products", key = "#productId")
    public ProductDTO uploadImageByProductId(Long supplierId, Long productId, String email, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
        String url;
        try {
            url = storageService.uploadFile(file, email);
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
        product.setImageUrl(url);
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDTO.class);
    }

    @Override
    public DownloadResponse downloadImageByProductId(Long productId, Long supplierId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
        if(!product.getSupplierId().equals(supplierId)) {
            throw new APIException("Product does not belong to the given supplier");
        }
        DownloadResponse downloadResponse = storageService.downloadFileEnhanced(product.getImageUrl());
        downloadResponse.setFileName(product.getImageUrl());
        return downloadResponse;
    }
}
