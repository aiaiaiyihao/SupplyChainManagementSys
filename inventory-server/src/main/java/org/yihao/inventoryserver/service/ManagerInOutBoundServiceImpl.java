package org.yihao.inventoryserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.yihao.inventoryserver.Exception.APIException;
import org.yihao.inventoryserver.Exception.ResourceNotFoundException;
import org.yihao.inventoryserver.Specification.InventoryMovementSpecification;
import org.yihao.inventoryserver.model.Inventory;
import org.yihao.inventoryserver.model.InventoryMovement;
import org.yihao.inventoryserver.repository.InventoryMovementRepository;
import org.yihao.inventoryserver.repository.InventoryRepository;
import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryMovementResponse;
import org.yihao.shared.ENUMS.MovementType;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManagerInOutBoundServiceImpl implements ManagerInOutBoundService {
    private final ModelMapper modelMapper;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryRepository InventoryRepository;

    public ManagerInOutBoundServiceImpl(ModelMapper modelMapper
            , InventoryMovementRepository inventoryMovementMovementRepository
            , InventoryRepository InventoryRepository) {
        this.modelMapper = modelMapper;
        this.inventoryMovementRepository = inventoryMovementMovementRepository;
        this.InventoryRepository = InventoryRepository;
    }

    @Override
    public InventoryMovementResponse getAllInoutBound(MovementType movementType, Long inventoryId, Long productId, String productName, Long warehouseId, Boolean createdToday, String sortBy, boolean desc, Integer pageNumber, Integer pageSize) {
        Sort sort = desc? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sort);
        Specification<InventoryMovement> specification
                = Specification.where(InventoryMovementSpecification.hasProductId(productId))
                .and(InventoryMovementSpecification.hasInventoryMovementType(movementType))
                .and(InventoryMovementSpecification.hasInventoryId(inventoryId))
                .and(InventoryMovementSpecification.hasProductName(productName))
                .and(InventoryMovementSpecification.hasWarehouseId(warehouseId));
        if (Boolean.TRUE.equals(createdToday)) {
            specification = specification.and(InventoryMovementSpecification.isCreatedToday());
        }
        Page<InventoryMovement> inventoryMovementInPage = inventoryMovementRepository.findAll(specification,pageDetails);
        List<InventoryMovement> inventoryMovementList = inventoryMovementInPage.getContent();
        if(inventoryMovementList.isEmpty()) throw new APIException("No inventoryMovement found");
        List<InventoryMovementDTO> inventoryMovementDTOS = inventoryMovementList.stream()
                .map(inventoryMovement -> modelMapper.map(inventoryMovement, InventoryMovementDTO.class)).toList();
        InventoryMovementResponse inventoryMovementResponse = new InventoryMovementResponse();
        inventoryMovementResponse.setInventoryMovementDTO(inventoryMovementDTOS);
        inventoryMovementResponse.setPageNumber(inventoryMovementInPage.getNumber());
        inventoryMovementResponse.setPageSize(inventoryMovementInPage.getSize());
        inventoryMovementResponse.setTotalPages(inventoryMovementInPage.getTotalPages());
        inventoryMovementResponse.setTotalElements(inventoryMovementInPage.getTotalElements());
        inventoryMovementResponse.setLastPage(inventoryMovementInPage.isLast());
        return inventoryMovementResponse;
    }

    @Override
    public InventoryMovementDTO updateInOutBoundById(Long inventoryMovementId, InventoryMovementDTO inventoryMovementDTO) {
        InventoryMovement inventoryMovementFound = inventoryMovementRepository.findById(inventoryMovementId)
                .orElseThrow(()->new ResourceNotFoundException("InventoryMovement","id",inventoryMovementId));
        Inventory inventory = inventoryMovementFound.getInventory();

        Integer oldQuantity = inventoryMovementFound.getQuantity();
        Integer newQuantity = inventoryMovementDTO.getQuantity();

        MovementType movementType = inventoryMovementFound.getMovementType();
        int updatedInventoryQty;
        if(movementType.equals(MovementType.INBOUND)){
            updatedInventoryQty = inventory.getQuantity()-oldQuantity+newQuantity;
        } else{
            updatedInventoryQty= inventory.getQuantity()+oldQuantity-newQuantity;
        }
        if(updatedInventoryQty<0) throw new APIException("Resulting inventory quantity cannot be negative");
        inventoryMovementFound.setQuantity(newQuantity);
        inventoryMovementFound.setReason(inventoryMovementDTO.getReason());
        inventoryMovementFound.setUpdatedAt(LocalDateTime.now());
        inventory.setQuantity(updatedInventoryQty);
        InventoryRepository.saveAndFlush(inventory);
        InventoryMovement saved = inventoryMovementRepository.save(inventoryMovementFound);

        return modelMapper.map(saved, InventoryMovementDTO.class);
    }
}
