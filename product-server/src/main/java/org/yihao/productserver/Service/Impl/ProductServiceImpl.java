package org.yihao.productserver.Service.Impl;

import jakarta.persistence.OrderBy;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yihao.productserver.Specification.ProductSpecification;
import org.yihao.shared.DTOS.*;
import org.yihao.productserver.Exception.APIException;
import org.yihao.productserver.Exception.ResourceNotFoundException;
import org.yihao.productserver.Repository.ProductRepository;
import org.yihao.productserver.Service.ProductService;
import org.yihao.productserver.model.Product;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO addProduct(ProductDTO productDTO) {
        if (productRepository.existsProductByProductName(productDTO.getProductName())) {
            throw new APIException("Product name already exists");
        } else {
            Product addProduct = modelMapper.map(productDTO, Product.class);
            Product savedProduct = productRepository.save(addProduct);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
    }

    @Override
    public ProductResponse getAllProducts(Long productId, String productName, Long supplierId,
                                          ProductPhase productPhase, ProductCategory productCategory,
                                          Long factoryId, Long warehouseId, Integer pageNumber,
                                          Integer pageSize, String sortBy, boolean desc) {
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Product> specification = Specification.where(ProductSpecification.hasProductId(productId)).and(ProductSpecification.hasProductNameLike(productName))
                .and(ProductSpecification.hasSupplierId(supplierId)).and(ProductSpecification.hasProductPhase(productPhase)).and(ProductSpecification.hasProductCategory(productCategory))
                .and(ProductSpecification.hasFactoryId(factoryId)).and(ProductSpecification.hasWarehouseId(warehouseId));
        Page<Product> productsInPage = productRepository.findAll(specification,pageDetails);
        List<Product> products = productsInPage.getContent();
        if (products.isEmpty()) {
            throw new APIException("No products found");
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
    /*Spring checks if "products" cache already has an entry with key productId*/
    @Cacheable(value = "products", key = "#productId")
    public ProductDTO getProductById(Long productId) {
        log.info("Fetching product from DB with id {}", productId);
        Product productFound = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return modelMapper.map(productFound, ProductDTO.class);
    }

    @Override
    @CachePut(value = "products", key = "#productId")
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {
        log.info("Fetching product from DB with id {}", productId);
        Product productFound = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
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
    public ProductDTO deleteProductById(Long productId) {
        log.info("Fetching product from DB with id {}", productId);
        Product productFound = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        productRepository.deleteById(productId);
        return modelMapper.map(productFound, ProductDTO.class);
    }


}
