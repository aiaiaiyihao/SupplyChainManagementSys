package org.yihao.inventoryserver.service;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yihao.inventoryserver.Exception.APIException;
import org.yihao.inventoryserver.Exception.ResourceNotFoundException;
import org.yihao.inventoryserver.model.Inventory;
import org.yihao.inventoryserver.model.InventoryMovement;
import org.yihao.inventoryserver.repository.InventoryMovementRepository;
import org.yihao.inventoryserver.repository.InventoryRepository;
import org.yihao.shared.DTOS.InventoryDTO;
import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryQueryDTO;
import org.yihao.shared.DTOS.InventoryResponse;
import org.yihao.shared.ENUMS.MovementType;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ManagerInventoryServiceImpl implements ManagerInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ModelMapper modelMapper;

    public ManagerInventoryServiceImpl(InventoryRepository inventoryRepository, ModelMapper modelMapper,
                                       InventoryMovementRepository inventoryMovementRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public InventoryResponse getAllInventoriesPage(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Inventory> inventoryInPage = inventoryRepository.findAll(pageDetails);
        List<Inventory> inventoryList = inventoryInPage.getContent();
        if (inventoryList.isEmpty()) throw new APIException("No inventory found");
        List<InventoryDTO> inventoryDTOS = inventoryList.stream()
                .map(inventory -> modelMapper.map(inventory, InventoryDTO.class)).toList();
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(inventoryDTOS);
        inventoryResponse.setPageNumber(inventoryInPage.getNumber());
        inventoryResponse.setPageSize(inventoryInPage.getSize());
        inventoryResponse.setTotalPages(inventoryInPage.getTotalPages());
        inventoryResponse.setTotalElements(inventoryInPage.getTotalElements());
        inventoryResponse.setLastPage(inventoryInPage.isLast());
        return inventoryResponse;
    }

    @Override
    public InventoryDTO getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventoryId", inventoryId));
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Optional<Inventory> inventory = inventoryRepository
                .findByProductIdAndWarehouseId(inventoryDTO.getProductId(), inventoryDTO.getWarehouseId());
        if (inventory.isPresent())
            throw new APIException("Inventory already exists with given productId and warehouseId");
        Inventory inventoryToSave = modelMapper.map(inventoryDTO, Inventory.class);
        inventoryToSave.setCreatedAt(LocalDateTime.now());
        inventoryToSave.setLastUpdated(LocalDateTime.now());
        Inventory inventorySaved = inventoryRepository.save(inventoryToSave);
        return modelMapper.map(inventorySaved, InventoryDTO.class);
    }

    @Override
    public InventoryDTO getInventoryByProductIdAndWarehouseId(InventoryQueryDTO queryDTO) {
        Inventory inventory = inventoryRepository
                .findByProductIdAndWarehouseId(queryDTO.getProductId(), queryDTO.getWarehouseId())
                .orElseThrow(() -> new APIException("No inventory found with productId: " + queryDTO.getProductId()
                        + " and warehouseId: " + queryDTO.getWarehouseId()));
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    @Override
    public InventoryDTO updateInventoryById(Long inventoryId, InventoryDTO inventoryDTO) {
        Inventory inventoryFound = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventoryId", inventoryId));
        if (inventoryDTO.getProductId() != null) {
            inventoryFound.setProductId(inventoryDTO.getProductId());
        }
        if (inventoryDTO.getWarehouseId() != null) {
            inventoryFound.setWarehouseId(inventoryDTO.getWarehouseId());
        }
        if (inventoryDTO.getQuantity() != null) {
            inventoryFound.setQuantity(inventoryDTO.getQuantity());
        }
        if (inventoryDTO.getProductName() != null) {
            inventoryFound.setProductName(inventoryDTO.getProductName());
        }
        inventoryFound.setLastUpdated(LocalDateTime.now());
        Inventory inventorySaved = inventoryRepository.save(inventoryFound);
        return modelMapper.map(inventorySaved, InventoryDTO.class);
    }

    @Override
    public InventoryDTO updateInventoryByMovement(InventoryMovementDTO inventoryMovementDTO) {
        Long productId = inventoryMovementDTO.getProductId();
        Long warehouseId = inventoryMovementDTO.getWarehouseId();
        Optional<Inventory> inventoryOptional = inventoryRepository
                .findByProductIdAndWarehouseId(productId, warehouseId);
        //找到有对应productId 和 warehouseId的库存 要创建
        if (inventoryOptional.isEmpty()) {
            //没有库存要出库 这是不可能的
            if (inventoryMovementDTO.getMovementType().equals(MovementType.OUTBOUND)) {
                throw new APIException("No Inventory found with given productId and warehouseId for outbount movement");
            } else {
                //没有库存 要入库 就要创建新的库存
                Inventory inventoryNew = new Inventory();
                inventoryNew.setProductId(productId);
                inventoryNew.setWarehouseId(warehouseId);
                inventoryNew.setProductName(inventoryMovementDTO.getProductName());
                inventoryNew.setQuantity(inventoryMovementDTO.getQuantity());
                inventoryNew.setCreatedAt(LocalDateTime.now());
                inventoryNew.setLastUpdated(LocalDateTime.now());
                Inventory saved = inventoryRepository.save(inventoryNew);
                InventoryMovement inventoryMovement = modelMapper.map(inventoryMovementDTO, InventoryMovement.class);
                inventoryMovement.setInventory(saved);
                inventoryMovement.setReason(inventoryMovementDTO.getReason());
                inventoryMovement.setCreatedAt(LocalDateTime.now());
                inventoryMovement.setUpdatedAt(LocalDateTime.now());
                inventoryMovementRepository.save(inventoryMovement);
                return modelMapper.map(saved, InventoryDTO.class);
            }
            //找到有对应productId 和 warehouseId的库存
        } else {
            Inventory inventoryFound = inventoryOptional.get();
            Integer amountChanged = inventoryMovementDTO.getMovementType()
                    .equals(MovementType.INBOUND) ?
                    inventoryMovementDTO.getQuantity() : (-inventoryMovementDTO.getQuantity());
            int newAmount = inventoryFound.getQuantity() + amountChanged;
            if (newAmount < 0) throw new APIException("Not enough inventory left for Outbound Movement. Inventory left: "
                    +inventoryFound.getQuantity()+", movementQuantity: "+inventoryMovementDTO.getQuantity());
            inventoryFound.setQuantity(newAmount);
            inventoryFound.setLastUpdated(LocalDateTime.now());
            Inventory inventorySaved = inventoryRepository.save(inventoryFound);
            InventoryMovement inventoryMovement = modelMapper.map(inventoryMovementDTO, InventoryMovement.class);
            inventoryMovement.setInventory(inventorySaved);
            inventoryMovement.setCreatedAt(LocalDateTime.now());
            inventoryMovement.setUpdatedAt(LocalDateTime.now());
            inventoryMovement.setReason(inventoryMovementDTO.getReason());
            inventoryMovementRepository.save(inventoryMovement);
            return modelMapper.map(inventorySaved, InventoryDTO.class);
        }
    }


}

