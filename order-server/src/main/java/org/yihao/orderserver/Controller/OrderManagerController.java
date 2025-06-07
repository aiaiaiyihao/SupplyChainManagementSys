package org.yihao.orderserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.orderserver.Exception.APIException;
import org.yihao.orderserver.Service.OrderManagerService;
import org.yihao.orderserver.config.AppConstants;
import org.yihao.shared.DTOS.OrderDTO;
import org.yihao.shared.DTOS.OrderResponse;
import org.yihao.shared.DTOS.OrderStatusUpdateRequest;
import org.yihao.shared.ENUMS.OrderStatus;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.ENUMS.WarehouseStatus;

@RestController
@RequestMapping("/admin/orders")
public class OrderManagerController {
    private final OrderManagerService orderManagerService;

    public OrderManagerController(OrderManagerService orderManagerService) {
        this.orderManagerService = orderManagerService;
    }

    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestHeader("role") String role,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) Long deliveryId,
            @RequestParam(required = false) Long factoryId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean desc
            ) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN CHECK ALL ORDERS...");
        }
        OrderResponse orderDTOS = orderManagerService.getAllOrders(productId,productName,supplierId,supplierName,
                orderStatus,deliveryId,factoryId,warehouseId,pageNumber, pageSize,sortBy,desc);
        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN CHECK ORDER...");
        }
        OrderDTO orderDTO = orderManagerService.getOrderById(id);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader("role") String role, @RequestBody OrderDTO orderDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN CREATE ORDER...");
        }
        OrderDTO orderDTOCreated = orderManagerService.createOrder(orderDTO);
        return new ResponseEntity<>(orderDTOCreated, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<OrderDTO> cloneOrder(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN CLONE ORDER...");
        }
        OrderDTO orderDTOCreated = orderManagerService.cloneOrder(id);
        return new ResponseEntity<>(orderDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestHeader("role") String role,
            @PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN UPDATE ORDER...");
        }
        OrderDTO orderDTOUpdated = orderManagerService.updateById(id, orderDTO);
        return new ResponseEntity<>(orderDTOUpdated, HttpStatus.OK);
    }

    @PatchMapping("/hold/{id}")
    public ResponseEntity<OrderDTO> holdOrder(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN HOLD ORDER...");
        }
        OrderDTO orderDTOUpdated = orderManagerService.holdOrder(id);
        return new ResponseEntity<>(orderDTOUpdated, HttpStatus.OK);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<OrderDTO> cancelOrder(
            @RequestHeader("role") String role,@PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN CANCEL ORDER...");
        }
        OrderDTO orderDTOUpdated = orderManagerService.cancelOrder(id);
        return new ResponseEntity<>(orderDTOUpdated, HttpStatus.OK);
    }

    @PutMapping("/delivery")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @RequestHeader("role") String role,
            @RequestBody OrderStatusUpdateRequest request) {
        if (!role.equals(Role.DRIVER.name()) && !role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager and Driver can update Order Status");
        }
        OrderDTO orderDTOUpdated = orderManagerService.changeOrderStatus(request);
        return new ResponseEntity<>(orderDTOUpdated, HttpStatus.OK);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<OrderDTO> requestReturnOrder(
            @RequestHeader("role") String role, @PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("Only Manager can request return Order");
        }
        OrderDTO orderDTOUpdate = orderManagerService.returnOrder(id);
        return new ResponseEntity<>(orderDTOUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrder(
            @RequestHeader("role") String role,@PathVariable Long id) {
        if (!role.equals(Role.MANAGER.name())) {
            throw new APIException("ONLY MANAGER CAN DELETE ORDER...");
        }
        OrderDTO deletedOrderDTO = orderManagerService.deleteById(id);
        return new ResponseEntity<>(deletedOrderDTO, HttpStatus.OK);
    }


}
