package org.yihao.ordermanagementserver.service;

import org.yihao.shared.DTOS.CreateOrderRequest;
import org.yihao.shared.DTOS.OrderApprovalDTO;
import org.yihao.shared.DTOS.OrderDTO;

public interface OrderManagementService {
    OrderDTO createOrder(String role, CreateOrderRequest createOrderRequest);

    OrderDTO approveOrder(String role, Long tableId, OrderApprovalDTO orderApprovalDTO);

    OrderDTO requestReturnOrder(String role, Long orderId);
}
