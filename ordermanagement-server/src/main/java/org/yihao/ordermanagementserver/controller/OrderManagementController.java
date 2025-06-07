package org.yihao.ordermanagementserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.ordermanagementserver.Exception.APIException;
import org.yihao.shared.DTOS.CreateOrderRequest;
import org.yihao.ordermanagementserver.service.OrderManagementService;
import org.yihao.shared.DTOS.OrderApprovalDTO;
import org.yihao.shared.DTOS.OrderDTO;
import org.yihao.shared.ENUMS.Role;

@RestController
public class OrderManagementController {
    private OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @PostMapping("/admin/ordermanage")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader("role") String role,
            @RequestBody CreateOrderRequest createOrderRequest) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Generate Orders");
        }
        OrderDTO orderDTO = orderManagementService.createOrder(role,createOrderRequest);
        return ResponseEntity.ok(orderDTO);
    }

    @PatchMapping("/supplier/approve")
    public ResponseEntity<OrderDTO> approveOrder(
            @RequestHeader("role") String role,@RequestHeader("tableId") Long tableId,
            @RequestBody OrderApprovalDTO orderApprovalDTO) {
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("Only Supplier Can Approve Orders");
        }
        OrderDTO orderDTO = orderManagementService.approveOrder(role,tableId,orderApprovalDTO);
        return ResponseEntity.ok(orderDTO);

    }

    @PutMapping("/admin/ordermanagement/return/{orderId}")
    public ResponseEntity<OrderDTO> requestReturnOrder(
            @RequestHeader("role") String role,
            @PathVariable Long orderId) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Request For Return Orders");
        }
        OrderDTO orderDTO = orderManagementService.requestReturnOrder(role,orderId);
        return ResponseEntity.ok(orderDTO);
    }
}
