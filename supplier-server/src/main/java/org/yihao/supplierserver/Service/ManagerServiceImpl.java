package org.yihao.supplierserver.Service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.*;
import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Exception.ResourceNotFoundException;
import org.yihao.supplierserver.Model.Supplier;
import org.yihao.supplierserver.Repository.SupplierRepository;

import java.util.List;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    public ManagerServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    @CachePut(value = "supplier", key = "#supplierId")
    @Override
    public SupplierStatusDTO offBoardSupplierBySupplierId(Long supplierId) {
        Supplier supplierFound = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "SupplierId", supplierId));
        supplierFound.setSupplierstatus(SupplierStatus.OFFBOARD);
        supplierRepository.save(supplierFound);
        SupplierStatusDTO supplierStatusDTO = new SupplierStatusDTO();
        supplierStatusDTO.setSupplierName(supplierFound.getSupplierName());
        supplierStatusDTO.setSupplierstatus(supplierFound.getSupplierstatus());
        return supplierStatusDTO;
    }

    @Override
    public SupplierPageResponse getAllSuppliers(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);

        Page<Supplier> suppliersInPage = supplierRepository.findAll(pageDetails);
        List<Supplier> suppliers = suppliersInPage.getContent();
        if(suppliers.isEmpty()) throw new APIException("No Suppliers Found");
        List<SupplierDTO> supplierDTOS = suppliers.stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDTO.class)).toList();
        SupplierPageResponse supplierResponse = new SupplierPageResponse();
        supplierResponse.setContent(supplierDTOS);
        supplierResponse.setPageNumber(suppliersInPage.getNumber());
        supplierResponse.setPageSize(suppliersInPage.getSize());
        supplierResponse.setTotalPages(suppliersInPage.getTotalPages());
        supplierResponse.setTotalElements(suppliersInPage.getTotalElements());
        supplierResponse.setLastPage(suppliersInPage.isLast());
        return supplierResponse;
    }

    @Cacheable(value = "supplier", key = "#supplierId")
    @Override
    public SupplierDTO getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "SupplierId", supplierId));
        return modelMapper.map(supplier, SupplierDTO.class);
    }
}
