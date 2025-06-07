package org.yihao.ordermanagementserver.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.OrderApprovalDTO;
import org.yihao.shared.DTOS.OrderDTO;

@FeignClient("order-server")
public interface RemoteOrderService {
    @PostMapping("/admin/orders")
    ResponseEntity<OrderDTO> createOrder(@RequestHeader("role") String role, @RequestBody OrderDTO orderDTO);

    @PutMapping("/supplier/orders")
    ResponseEntity<OrderDTO> approveOrder(@RequestHeader("role") String role,@RequestHeader("tableId") Long supplierId,
                                          @RequestBody OrderApprovalDTO approval);

    @PutMapping("admin/orders/return/{id}")
    ResponseEntity<OrderDTO> requestReturnOrder(@RequestHeader("role") String role,@PathVariable Long id);
}
