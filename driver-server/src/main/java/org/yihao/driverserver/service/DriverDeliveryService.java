package org.yihao.driverserver.service;


import org.yihao.driverserver.model.Delivery;
import org.yihao.shared.DTOS.*;

import java.util.List;

public interface DriverDeliveryService {
    DeliveryResponse getDeliveryByDriverId(Long driverId, Integer pageNumber, Integer pageSize);
    List<Delivery> getDeliveryByDriverId(Long driverId);

    DeliveryDTO updateDeliveryStatus(Long driverId, DeliveryStatusChangeRequest request);

}
