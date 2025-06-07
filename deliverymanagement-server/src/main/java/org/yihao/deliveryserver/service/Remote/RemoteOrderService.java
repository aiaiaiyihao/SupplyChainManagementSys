package org.yihao.deliveryserver.service.Remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.*;



@FeignClient("order-server")
public interface RemoteOrderService {
    @PutMapping("admin/orders/delivery")
    ResponseEntity<OrderDTO> updateOrderStatus(
            @RequestHeader("role") String role,
            @RequestBody OrderStatusUpdateRequest request
    );

    @GetMapping("admin/orders/{id}")
    ResponseEntity<OrderDTO> getOrderById(
            @RequestHeader("role") String role,
            @PathVariable Long id
    );

    @PutMapping("admin/orders/{id}")
    ResponseEntity<OrderDTO> updateOrder(
            @RequestHeader("role") String role,
            @PathVariable Long id, @RequestBody OrderDTO orderDTO
    );
}
