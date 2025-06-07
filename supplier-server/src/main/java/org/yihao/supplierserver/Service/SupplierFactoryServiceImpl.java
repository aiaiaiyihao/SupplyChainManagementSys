package org.yihao.supplierserver.Service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.*;

import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Exception.ResourceNotFoundException;
import org.yihao.supplierserver.Model.Factory;
import org.yihao.supplierserver.Model.Supplier;
import org.yihao.supplierserver.Repository.FactoryRepository;
import org.yihao.supplierserver.Repository.SupplierRepository;

import java.util.List;

@Service
@Transactional
public class SupplierFactoryServiceImpl implements SupplierFactoryService {
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final FactoryRepository factoryRepository;

    public SupplierFactoryServiceImpl(ModelMapper modelMapper
            , SupplierRepository supplierRepository, FactoryRepository factoryRepository) {
        this.modelMapper = modelMapper;
        this.supplierRepository = supplierRepository;
        this.factoryRepository = factoryRepository;
    }
    @Override
    public FactoryPageResponse findSupplierFactoriesBySupplierId(Integer pageNumber, Integer pageSize, Long supplierId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "Supplier Id", supplierId)
        );
        Page<Factory> factoriesInPage = factoryRepository.findFactoriesBySupplier(supplier, pageDetails);
        List<Factory> factories = factoriesInPage.getContent();
        if(factoriesInPage.isEmpty()){
            throw new APIException("Supplier has no factories");
        } else{
            List<FactoryDTO> factoryDTOS = factories.stream()
                    .map(factory -> modelMapper.map(factory, FactoryDTO.class)).toList();
            FactoryPageResponse factoryResponse = new FactoryPageResponse();
            factoryResponse.setContent(factoryDTOS);
            factoryResponse.setPageNumber(factoriesInPage.getNumber());
            factoryResponse.setPageSize(factoriesInPage.getSize());
            factoryResponse.setTotalPages(factoriesInPage.getTotalPages());
            factoryResponse.setTotalElements(factoriesInPage.getTotalElements());
            factoryResponse.setLastPage(factoriesInPage.isLast());
            return factoryResponse;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "factory", key = "#factoryId"),
            @CacheEvict(value = "address", allEntries = true)
    })
    @Override
    public FactoryDTO deleteFactoryById(Long supplierId, Long factoryId) {
        Factory factoryFound = factoryRepository.findByFactoryIdAndSupplier_SupplierId(factoryId, supplierId)
                .orElseThrow(() -> new APIException("Factory does not exist with factoryId: "
                        + factoryId + " of supplierId: " + supplierId));

        factoryRepository.deleteByFactoryIdAndSupplier_SupplierId(factoryId, supplierId);


        return modelMapper.map(factoryFound, FactoryDTO.class);
    }

    @CachePut(value = "factory", key = "#factoryId")
    @CacheEvict(value = "address", allEntries = true)
    @Transactional
    @Override
    public FactoryDTO updateFactoryById(Long supplierId, Long factoryId, FactoryDTO factoryDTO) {
        Factory factoryFound = factoryRepository.findByFactoryIdAndSupplier_SupplierId(factoryId, supplierId)
                .orElseThrow(()->new APIException("Factory does not exist with factoryId: " + factoryId
                        +" of supplierId: " + supplierId));
        if(!factoryDTO.getFactoryName().equals(factoryFound.getFactoryName())&&
                factoryRepository.existsFactoryByFactoryName(factoryDTO.getFactoryName())){
            throw new APIException("Factory already exists with factoryName: " + factoryDTO.getFactoryName());
        }
        factoryFound.setFactoryName(factoryDTO.getFactoryName());
        org.yihao.supplierserver.Model.Address newAddress = modelMapper.map(factoryDTO.getFactoryAddress()
                , org.yihao.supplierserver.Model.Address.class);
        newAddress.setAddressId(null); // or ensure ID is not copied in the first place
        factoryFound.setFactoryAddress(newAddress);
        factoryFound.setFactoryImagesUrl(factoryDTO.getFactoryImagesUrl());
        factoryFound.setFactoryStatus(factoryDTO.getFactoryStatus());
        factoryRepository.save(factoryFound);
        return modelMapper.map(factoryFound, FactoryDTO.class);
    }

    @CachePut(value = "factory", key = "#result.factoryId")
    @Transactional
    @Override
    public FactoryDTO createFactoryById(Long supplierId, FactoryDTO factoryDTO) {
        if(factoryRepository.existsFactoryByFactoryName(factoryDTO.getFactoryName())){
            throw new APIException("Factory already exists with factoryName: " + factoryDTO.getFactoryName());
        }
        Factory newFactory = modelMapper.map(factoryDTO, Factory.class);
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "Supplier Id", supplierId));
        newFactory.setSupplier(supplier);
        supplier.addFactory(newFactory);
        if (newFactory.getFactoryAddress() != null) {
            newFactory.setFactoryAddress(newFactory.getFactoryAddress());
        }
        Factory saved = factoryRepository.save(newFactory);
        return modelMapper.map(saved, FactoryDTO.class);
//        Supplier supplierFound = supplierRepository.findById(supplierId).orElseThrow(
//                () -> new APIException("Supplier not found"));
//        supplierFound.addFactory(newFactory);
//        Supplier saved = supplierRepository.save(supplierFound);
//        return modelMapper.map(newFactory, FactoryDTO.class);
    }

    @Cacheable(value="address",key="#factoryId")
    @Override
    public Address findFactoryAddressByFactoryId(Long supplierId, Long factoryId) {
        Factory factoryFound = factoryRepository.findById(factoryId)
                .orElseThrow(()->new ResourceNotFoundException("Factory", "factoryId", factoryId));
        if(!factoryFound.getSupplier().getSupplierId().equals(supplierId)){
            throw new APIException("Supplier does not own factoryId: " + factoryId);
        }
        org.yihao.supplierserver.Model.Address factoryAddress = factoryFound.getFactoryAddress();
        return modelMapper.map(factoryAddress, Address.class);
    }

    @Override
    public FactoryPageResponse getAllFactories(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Factory> factoriesInPage = factoryRepository.findAll(pageDetails);
        List<Factory> factories = factoriesInPage.getContent();
        if(factories.isEmpty()){
            throw new APIException("No factory exists");
        }
        List<FactoryDTO> factoryDTOS = factories.stream()
                .map(factory -> modelMapper.map(factory, FactoryDTO.class)).toList();
        FactoryPageResponse factoryResponse = new FactoryPageResponse();
        factoryResponse.setContent(factoryDTOS);
        factoryResponse.setPageNumber(factoriesInPage.getNumber());
        factoryResponse.setPageSize(factoriesInPage.getSize());
        factoryResponse.setTotalPages(factoriesInPage.getTotalPages());
        factoryResponse.setTotalElements(factoriesInPage.getTotalElements());
        factoryResponse.setLastPage(factoriesInPage.isLast());
        return factoryResponse;
    }

    @Override
    public FactoryDTO findSupplierFactoryByFactoryId(Long supplierId, Long factoryId) {
        Factory factoryFound = factoryRepository.findById(factoryId)
                .orElseThrow(()->new ResourceNotFoundException("Factory", "factoryId", factoryId));

        if(!factoryFound.getSupplier().getSupplierId().equals(supplierId)){
            throw new APIException("Supplier does not own factoryId: " + factoryId);
        }
        return modelMapper.map(factoryFound, FactoryDTO.class);
    }
}
