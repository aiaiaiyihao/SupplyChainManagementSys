package org.yihao.facilityserver.Service;

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
import org.yihao.facilityserver.Exception.APIException;
import org.yihao.facilityserver.Exception.ResourceNotFoundException;
import org.yihao.facilityserver.Repository.WarehouseRepository;
import org.yihao.facilityserver.Specification.WarehouseSpecification;
import org.yihao.facilityserver.model.Warehouse;
import org.yihao.shared.DTOS.WarehouseDTO;
import org.yihao.shared.DTOS.WarehouseResponse;
import org.yihao.shared.ENUMS.WarehouseStatus;

import java.util.List;

@Service
@Slf4j
public class ManagerWarehouseServiceImpl implements ManagerWarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;
    public ManagerWarehouseServiceImpl(WarehouseRepository warehouseRepository, ModelMapper modelMapper) {
        this.warehouseRepository = warehouseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public WarehouseResponse getAllWarehouses(Integer pageNumber, Integer pageSize, WarehouseStatus status, Long zipCode, String sortBy, boolean desc) {
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sort);
        Specification<Warehouse> specification = Specification.where(WarehouseSpecification.hasStatus(status))
                .and(WarehouseSpecification.hasZipcode(zipCode));
        Page<Warehouse> warehouseInPage = warehouseRepository.findAll(specification,pageDetails);
        List<Warehouse> warehouses = warehouseInPage.getContent();
        if(warehouses.isEmpty()){
            throw new APIException("No warehouses found");
        }
        List<WarehouseDTO> warehouseDTOS = warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class)).toList();
        WarehouseResponse warehouseResponse = new WarehouseResponse();
        warehouseResponse.setContent(warehouseDTOS);
        warehouseResponse.setPageNumber(warehouseInPage.getNumber());
        warehouseResponse.setPageSize(warehouseInPage.getSize());
        warehouseResponse.setTotalPages(warehouseInPage.getTotalPages());
        warehouseResponse.setTotalElements(warehouseInPage.getTotalElements());
        warehouseResponse.setLastPage(warehouseInPage.isLast());
        return warehouseResponse;
    }

    @Override
    @Cacheable(value = "warehouse", key = "#warehouseId")
    public WarehouseDTO getWarehouseById(Long warehouseId) {
        log.info("Fetching warehouse from DB with id {}", warehouseId);
        Warehouse warehouseFound = warehouseRepository
                .findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse", "WarehouseId", warehouseId));
        return modelMapper.map(warehouseFound, WarehouseDTO.class);
    }

    @Override
    @CacheEvict(value = "warehouse", allEntries = true)
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = modelMapper.map(warehouseDTO, Warehouse.class);
        if(warehouseRepository.existsWarehouseByWarehouseName(warehouse.getWarehouseName())){
            throw new APIException("Warehouse name already exists");
        }
        Warehouse saved = warehouseRepository.save(warehouse);
        return modelMapper.map(saved, WarehouseDTO.class);
    }

    @Override
    @CachePut(value = "warehouse", key = "#warehouseId")
    public WarehouseDTO updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO) {
        log.info("Fetching warehouse from DB with id {}", warehouseId);
        Warehouse warehouseFound = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "WarehouseId", warehouseId));
        Warehouse warehouse = modelMapper.map(warehouseDTO, Warehouse.class);
        String name = warehouse.getWarehouseName();
        //name changed. cannot be redundant
        if(name!=null && !name.equals(warehouseFound.getWarehouseName())){
            if(warehouseRepository.existsWarehouseByWarehouseName(name)){
                throw new APIException("Warehouse name already exists");
            }
            warehouseFound.setWarehouseName(name);
        }
        if(warehouse.getAddress()!=null){
            warehouseFound.setAddress(warehouse.getAddress());
        }
        if(warehouse.getContactNumber()!=null){
            warehouseFound.setContactNumber(warehouse.getContactNumber());
        }
        Warehouse saved = warehouseRepository.save(warehouseFound);
        return modelMapper.map(saved, WarehouseDTO.class);
    }

    @Override
    @CacheEvict(value = "warehouse", key = "#warehouseId")
    public WarehouseDTO deleteWarehouse(Long warehouseId) {
        log.info("Fetching warehouse from DB with id {}", warehouseId);
        Warehouse warehouseFound = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "WarehouseId", warehouseId));
        if(!warehouseFound.getStatus().equals(WarehouseStatus.CLOSED)){
            throw new APIException("Warehouse is not closed, You cannot delete this warehouse");
        }
        warehouseRepository.deleteById(warehouseId);
        return modelMapper.map(warehouseFound, WarehouseDTO.class);
    }

    @Override
    @CachePut(value = "warehouse", key = "#warehouseDTO.warehouseId")
    public WarehouseDTO changeWarehouseStatus(WarehouseDTO warehouseDTO) {
        log.info("Fetching warehouse from DB with id {}", warehouseDTO.getWarehouseId());
        Long warehouseId = warehouseDTO.getWarehouseId();
        Warehouse warehouseFound = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "WarehouseId", warehouseId));
        warehouseFound.setStatus(warehouseDTO.getStatus());
        Warehouse saved = warehouseRepository.save(warehouseFound);
        return modelMapper.map(saved, WarehouseDTO.class);
    }


}
