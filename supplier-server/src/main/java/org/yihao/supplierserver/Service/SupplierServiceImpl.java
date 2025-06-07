package org.yihao.supplierserver.Service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.*;
import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Exception.ResourceNotFoundException;
import org.yihao.supplierserver.Model.Address;
import org.yihao.supplierserver.Model.Supplier;
import org.yihao.supplierserver.Repository.FactoryRepository;
import org.yihao.supplierserver.Repository.SupplierRepository;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final FactoryRepository factoryRepository;

    public SupplierServiceImpl(ModelMapper modelMapper
            , SupplierRepository supplierRepository, FactoryRepository factoryRepository) {
        this.modelMapper = modelMapper;
        this.supplierRepository = supplierRepository;
        this.factoryRepository = factoryRepository;
    }

    @CachePut(value = "supplier", key = "#result.supplierId")
    @Override
    public SupplierDTO createSupplierProfile(SupplierDTO supplierDTO) {
        if(supplierRepository.existsBySupplierName(supplierDTO.getSupplierName())) {
            throw new APIException("Supplier name already exists");
        }
        if(supplierRepository.existsByEmail(supplierDTO.getEmail())){
            throw new APIException("Supplier email already exists");
        }
        if(supplierRepository.existsByPhoneNumber(supplierDTO.getPhoneNumber())){
            throw new APIException("Supplier phone number already exists");
        }
        Supplier supplierProfile = modelMapper.map(supplierDTO, Supplier.class);
        supplierProfile.getAddress().setAddressType(AddressType.SUPPLIER);
        supplierProfile.setSupplierstatus(SupplierStatus.ONBOARD);
        Supplier supplierSaved = supplierRepository.save(supplierProfile);
        return modelMapper.map(supplierSaved, SupplierDTO.class);
    }


    @Cacheable(value = "supplier", key = "#supplierId")
    @Override
    public SupplierDTO getSupplierProfileBySupplierId(Long supplierId) {
        Supplier supplierFound = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "supplierId", supplierId));
        return modelMapper.map(supplierFound, SupplierDTO.class);
    }

    @CachePut(value = "supplier", key = "#supplierId")
    @Override
    public SupplierDTO updateSupplierProfileBySupplierId(SupplierDTO supplierDTO, Long supplierId) {
        Supplier supplierFound = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "supplierId", supplierId));
        if (supplierDTO.getAddress() != null) {
            Address existingAddress = supplierFound.getAddress();
            if (existingAddress != null) {
                // ✅ Update existing address fields instead of replacing it
                existingAddress.setLine1(supplierDTO.getAddress().getLine1());
                existingAddress.setLine2(supplierDTO.getAddress().getLine2());
                existingAddress.setCity(supplierDTO.getAddress().getCity());
                existingAddress.setState(supplierDTO.getAddress().getState());
                existingAddress.setZip(supplierDTO.getAddress().getZip());
                existingAddress.setCountry(supplierDTO.getAddress().getCountry());
            } else {
                // ✅ Only create a new address if one does not exist
                Address newAddress = new Address();
                newAddress.setLine1(supplierDTO.getAddress().getLine1());
                newAddress.setLine2(supplierDTO.getAddress().getLine2());
                newAddress.setCity(supplierDTO.getAddress().getCity());
                newAddress.setState(supplierDTO.getAddress().getState());
                newAddress.setZip(supplierDTO.getAddress().getZip());
                newAddress.setCountry(supplierDTO.getAddress().getCountry());
                supplierFound.setAddress(newAddress);
            }
        }
        supplierFound.setSupplierName(supplierDTO.getSupplierName());
        supplierFound.setEmail(supplierDTO.getEmail());
        supplierFound.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplierFound.setDocuments(supplierDTO.getDocuments());
        supplierFound.setIndustryType(supplierDTO.getIndustryType());
        supplierFound.setParentCompany(supplierDTO.getParentCompany());
        supplierFound.setWebPage( supplierDTO.getWebPage());
        supplierFound.setYearEstablished(supplierDTO.getYearEstablished());
        Supplier savedSupplier = supplierRepository.save(supplierFound);
        return modelMapper.map(savedSupplier, SupplierDTO.class);
    }

    @Caching(evict = {
            @CacheEvict(value = "address", allEntries = true),
            @CacheEvict(value = "factory", allEntries = true),
            @CacheEvict(value = "supplier", key = "#supplierId")
    })
    @Override
    public SupplierDTO deleteSupplierProfileBySupplierId(Long supplierId) {
        Supplier supplierFound = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "supplierId", supplierId));
        supplierRepository.deleteById(supplierId);
        return modelMapper.map(supplierFound, SupplierDTO.class);
    }



    /*@Override
    public SupplierFactoryDTO createSupplierFactories(SupplierFactoryDTO supplierFactoryDTO) {
        String supplierName = supplierFactoryDTO.getSupplierName();
        Supplier supplierFound = supplierRepository.findBySupplierName(supplierName).orElseThrow(
                () -> new APIException("Supplier not found"));

        //without it, supplierId will be null in address and factory table
        for (Factory factory : supplierFactoryDTO.getFactories()) {
            Address factoryAddress = factory.getFactoryAddress();

            factory.setSupplier(supplierFound);

            if (factoryAddress != null) {
                factoryAddress.setFactory(factory);
                factoryAddress.setAddressType(AddressType.FACTORY);
            }
        }
        supplierFound.setFactories(supplierFactoryDTO.getFactories());
        Supplier savedSupplierFactory = supplierRepository.save(supplierFound);
        return modelMapper.map(savedSupplierFactory, SupplierFactoryDTO.class);
    }*/
}
