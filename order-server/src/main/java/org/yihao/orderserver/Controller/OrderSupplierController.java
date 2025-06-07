package org.yihao.orderserver.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.orderserver.Exception.APIException;
import org.yihao.orderserver.Service.OrderSupplierService;
import org.yihao.orderserver.config.AppConstants;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;

@RestController
@RequestMapping("/supplier/orders")
public class OrderSupplierController {
    private final OrderSupplierService orderSupplierService;

    public OrderSupplierController(OrderSupplierService orderSupplierService) {
        this.orderSupplierService = orderSupplierService;
    }

    @GetMapping
    public ResponseEntity<OrderResponse> getOrderBySupplierId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if (!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("Only SUPPLIER CAN ACCESS ITS ORDERS");
        }
        OrderResponse orderResponse = orderSupplierService.getOrderBySupplierId(supplierId, pageNumber, pageSize);
        return ResponseEntity.ok(orderResponse);
    }

    //需要通过jwt获取supplierId 后续删除requestdto中的 supplierId
    @PutMapping
    public ResponseEntity<OrderDTO> approveOrder(
            @RequestHeader("role") String role,@RequestHeader("tableId") Long supplierId,
            @RequestBody OrderApprovalDTO approval) {
        if (!role.equals(Role.SUPPLIER.name())) {
            throw new APIException("Only SUPPLIER CAN APPROVE ITS ORDERS...");
        }
        OrderDTO orderDTOApproved = orderSupplierService.approveByOrderId(supplierId,approval);
        return ResponseEntity.ok(orderDTOApproved);
    }

}
