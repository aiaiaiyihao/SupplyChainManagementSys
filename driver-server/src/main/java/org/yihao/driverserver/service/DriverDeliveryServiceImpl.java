package org.yihao.driverserver.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.model.Delivery;
import org.yihao.driverserver.repository.DeliveryRepository;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DriverDeliveryServiceImpl implements DriverDeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public DriverDeliveryServiceImpl(
            DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
    }

            /*@Cacheable(value = "delivery", key = "#driverId")
        public List<Delivery> getDeliveryByDriverId(Long driverId)

        @Cacheable(value = "delivery", key = "#deliveryId")
        public Delivery getDeliveryByDeliveryId(Long deliveryId)
        Both methods write to the same cache "delivery" with key 1:

        getDeliveryByDriverId(1L) → caches key 1 with a List<Delivery>

        Then getDeliveryByDeliveryId(1L) → overwrites key 1 with a Delivery object

        💥 So the second call overwrites the first, and now:
        cacheManager.getCache("delivery").get(1)
        returns a Delivery object — not a list anymore.
        */
    @Override
    public List<Delivery> getDeliveryByDriverId(Long driverId) {
        log.info("Fetching order from DB with id {}", driverId);
        List<Delivery> deliveries = deliveryRepository.findByDriverId(driverId);
        if (deliveries.isEmpty()) throw new APIException("No delivery found");
        return deliveries;
    }

    @Override
    public DeliveryResponse getDeliveryByDriverId(Long driverId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Delivery> deliverysInPage = deliveryRepository.findByDriverId(driverId, pageDetails);
        List<Delivery> deliveries = deliverysInPage.getContent();
        if (deliveries.isEmpty()) throw new APIException("No deliveries found");
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
    @CachePut(value = "delivery", key = "#request.deliveryId")
    public DeliveryDTO updateDeliveryStatus(Long driverId, DeliveryStatusChangeRequest request) {
        log.info("Fetching order from DB with id {}", driverId);
        Long deliveryId = request.getDeliveryId();
        //see if the given deliveryId is that driver's
        Delivery deliveryFound = deliveryRepository.findByDriverIdAndDeliveryId(driverId, deliveryId)
                .orElseThrow(() -> new APIException("You have no delivery found with DeliveryId: " + deliveryId));

        if (deliveryFound.getDeliveryStatus().equals(DeliveryStatus.CANCELLED)) {
            throw new APIException("Delivery already cancelled");
        }
        if (deliveryFound.getDeliveryStatus().equals(DeliveryStatus.CREATED)) {
            throw new APIException("Delivery hasn't been appointed yet");
        }
        if (deliveryFound.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
            throw new APIException("Delivery already delivered");
        }
        //change to deliverying
        if (!request.getDeliveried()) {
            if (deliveryFound.getDeliveryStatus().equals(DeliveryStatus.DELIVERING)) {
                throw new APIException("Delivery No." + deliveryId + "is already being delivered");
            }
            deliveryFound.setDeliveryStatus(DeliveryStatus.DELIVERING);
            //change to deliveried
        } else {
            if (!deliveryFound.getDeliveryStatus().equals(DeliveryStatus.DELIVERING)) {
                throw new APIException("Change delivery status to DELIVERING first before change it to DELIVERIED");
            }
            deliveryFound.setDeliveryStatus(DeliveryStatus.DELIVERED);
        }
        deliveryFound.setUpdatedAt(LocalDateTime.now());
        Delivery saved = deliveryRepository.save(deliveryFound);
        return modelMapper.map(saved, DeliveryDTO.class);
    }
}
