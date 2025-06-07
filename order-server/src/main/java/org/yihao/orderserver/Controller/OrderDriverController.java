package org.yihao.orderserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.orderserver.Exception.APIException;
import org.yihao.orderserver.Service.OrderManagerService;
import org.yihao.shared.DTOS.OrderDTO;
import org.yihao.shared.DTOS.OrderStatusUpdateRequest;
import org.yihao.shared.ENUMS.Role;
//
//@RestController
//@RequestMapping("/driver/orders")
//public class OrderDriverController {
//    private final OrderManagerService orderManagerService;
//
//    public OrderDriverController(OrderManagerService orderManagerService) {
//        this.orderManagerService = orderManagerService;
//    }
//
//    @PutMapping("/delivery")
//    public ResponseEntity<OrderDTO> updateOrderStatusDriver(
//            @RequestHeader("role") String role,
//            @RequestBody OrderStatusUpdateRequest request) {
//        if(!role.equals(Role.DRIVER.name())){
//            throw new APIException("Only Driver can update Order Status");
//        }
//        OrderDTO orderDTOUpdated = orderManagerService.changeOrderStatus(request);
//        return new ResponseEntity<>(orderDTOUpdated, HttpStatus.OK);
//    }
//}
