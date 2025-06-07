package org.yihao.driverserver.service;


import org.yihao.shared.DTOS.DeliveryDTO;
import org.yihao.shared.DTOS.DeliveryResponse;

public interface ManagerDeliveryService {
    DeliveryResponse getAllDeliveries(Integer pageNumber, Integer pageSize);

    DeliveryDTO getDeliveryById(Long deliveryId);

    DeliveryDTO createDelivery(DeliveryDTO deliveryDTO);

    DeliveryDTO updateDeliveryById(Long deliveryId, DeliveryDTO deliveryDTO);

    DeliveryDTO deleteDeliveryById(Long deliveryId);

    DeliveryResponse getDeliveryByDriverId(Long driverId, Integer pageNumber, Integer pageSize);
}
