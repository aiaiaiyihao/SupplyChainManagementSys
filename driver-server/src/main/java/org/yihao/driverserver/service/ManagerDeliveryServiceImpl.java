package org.yihao.driverserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.Exception.ResourceNotFoundException;
import org.yihao.driverserver.model.Delivery;
import org.yihao.driverserver.repository.DeliveryRepository;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManagerDeliveryServiceImpl implements ManagerDeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public ManagerDeliveryServiceImpl(DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DeliveryResponse getAllDeliveries(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Delivery> deliverysInPage = deliveryRepository.findAll(pageDetails);
        List<Delivery> deliveries = deliverysInPage.getContent();
        if(deliveries.isEmpty()) throw new APIException("No deliveries found");
        List<DeliveryDTO> deliveryDTOs = deliveries.stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryDTO.class)).toList();
        DeliveryResponse deliveryResponse = new DeliveryResponse();
        deliveryResponse.setContent(deliveryDTOs);
        deliveryResponse.setPageNumber(deliverysInPage.getNumber());
        deliveryResponse.setPageSize(deliverysInPage.getSize());
        deliveryResponse.setTotalPages(deliverysInPage.getTotalPages());
        deliveryResponse.setTotalElements(deliverysInPage.getTotalElements());
        deliveryResponse.setLastPage(deliverysInPage.isLast());
        return deliveryResponse;
    }

    @Override
    @Cacheable(value = "delivery", key = "#deliveryId")
    public DeliveryDTO getDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery","DeliveryId",deliveryId));
        return modelMapper.map(delivery, DeliveryDTO.class);
    }

    @Override
    @CachePut(value = "delivery", key = "#result.deliveryId")
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Delivery newDelivery = modelMapper.map(deliveryDTO, Delivery.class);
        newDelivery.setCreatedAt(LocalDateTime.now());
        newDelivery.setUpdatedAt(LocalDateTime.now());
        if(deliveryDTO.getDriverId() != null) {
            newDelivery.setDeliveryStatus(DeliveryStatus.APPOINTED);
        } else{
            newDelivery.setDeliveryStatus(DeliveryStatus.CREATED);
        }
        LocalDateTime createdAt = newDelivery.getCreatedAt();
        newDelivery.setEstimatedDeliveryTime(createdAt.plusDays(7));
        Delivery deliverySaved = deliveryRepository.save(newDelivery);
        return modelMapper.map(deliverySaved, DeliveryDTO.class);
    }

    @Override
    @CachePut(value = "delivery", key = "#deliveryId")
    public DeliveryDTO updateDeliveryById(Long deliveryId, DeliveryDTO deliveryDTO) {
        Delivery deliveryFound = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "DeliveryId", deliveryId));

        /*It uses the ModelMapper library to automatically copy matching fields
        from the deliveryDTO object into the existing deliveryFound entity object.*/
        if (deliveryDTO.getOrderId() != null) {
            deliveryFound.setOrderId(deliveryDTO.getOrderId());
        }
        if (deliveryDTO.getDriverId() != null) {
            deliveryFound.setDriverId(deliveryDTO.getDriverId());
        }
        if (deliveryDTO.getDeliveryType() != null) {
            deliveryFound.setDeliveryType(deliveryDTO.getDeliveryType());
        }
        if (deliveryDTO.getWarehouseId() != null) {
            deliveryFound.setWarehouseId(deliveryDTO.getWarehouseId());
        }
        if (deliveryDTO.getFactoryId() != null) {
            deliveryFound.setFactoryId(deliveryDTO.getFactoryId());
        }
        if (deliveryDTO.getInstructions() != null) {
            deliveryFound.setInstructions(deliveryDTO.getInstructions());
        }
        if(deliveryDTO.getEstimatedDeliveryTime() != null) {
            deliveryFound.setEstimatedDeliveryTime(deliveryDTO.getEstimatedDeliveryTime());
        }

// Always update the timestamp
        deliveryFound.setUpdatedAt(LocalDateTime.now());
        Delivery deliveryUpdated = deliveryRepository.save(deliveryFound);
        return modelMapper.map(deliveryUpdated, DeliveryDTO.class);
    }

    @Override
    @CacheEvict(value = "delivery", key = "#deliveryId")
    public DeliveryDTO deleteDeliveryById(Long deliveryId) {
        Delivery deliveryFound = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "DeliveryId", deliveryId));
        if(deliveryFound.getDeliveryStatus()!= DeliveryStatus.CANCELLED){
            throw new APIException("You have to cancel the delivery first before deleting it");
        }
        deliveryRepository.deleteById(deliveryId);
        return modelMapper.map(deliveryFound, DeliveryDTO.class);
    }

    @Override
    public DeliveryResponse getDeliveryByDriverId(Long driverId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Delivery> deliverysInPage = deliveryRepository.findByDriverId(driverId,pageDetails);
        List<Delivery> deliveries = deliverysInPage.getContent();
        if(deliveries.isEmpty()) throw new APIException("No deliveries found");
        List<DeliveryDTO> deliveryDTOs = deliveries.stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryDTO.class)).toList();
        DeliveryResponse deliveryResponse = new DeliveryResponse();
        deliveryResponse.setContent(deliveryDTOs);
        deliveryResponse.setPageNumber(deliverysInPage.getNumber());
        deliveryResponse.setPageSize(deliverysInPage.getSize());
        deliveryResponse.setTotalPages(deliverysInPage.getTotalPages());
        deliveryResponse.setTotalElements(deliverysInPage.getTotalElements());
        deliveryResponse.setLastPage(deliverysInPage.isLast());
        return deliveryResponse;
    }


}
