package org.yihao.deliveryserver.service;

import org.yihao.shared.DTOS.DeliveryDTO;
import org.yihao.shared.DTOS.DeliveryStatusChangeRequest;

public interface DriverDeliveryManagementService {

    DeliveryDTO changeDriverStatus(String role, Long driverId, DeliveryStatusChangeRequest request);
}
