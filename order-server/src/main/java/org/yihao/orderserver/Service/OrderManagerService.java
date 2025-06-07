package org.yihao.orderserver.Service;

import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.OrderStatus;

public interface OrderManagerService {


    OrderDTO getOrderById(Long id);

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO cloneOrder(Long id);

    OrderDTO updateById(Long id, OrderDTO orderDTO);

    OrderDTO holdOrder(Long id);

    OrderDTO cancelOrder(Long id);

    OrderDTO deleteById(Long id);


    OrderResponse getAllOrders(Long productId, String productName, Long supplierId, String supplierName,
                               OrderStatus orderStatus, Long deliveryId, Long factoryId, Long warehouseId, Integer pageNumber, Integer pageSize, String sortBy, boolean desc);

    OrderDTO changeOrderStatus(OrderStatusUpdateRequest request);

    OrderDTO returnOrder(Long id);
}

