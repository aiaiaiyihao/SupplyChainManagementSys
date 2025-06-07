package org.yihao.deliveryserver.service;

import org.yihao.shared.DTOS.*;

public interface DeliverymanagementService {
    DeliveryDTO addDelivery(CreateDeliveryRequest orderDTO);
}
