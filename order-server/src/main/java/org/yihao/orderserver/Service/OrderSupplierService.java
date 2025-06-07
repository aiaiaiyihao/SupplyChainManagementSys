package org.yihao.orderserver.Service;

import org.yihao.shared.DTOS.*;

public interface OrderSupplierService {
    OrderResponse getOrderBySupplierId(Long supplierId, Integer pageNumber, Integer pageSize);

    OrderDTO approveByOrderId(Long supplierId, OrderApprovalDTO approval);

}
