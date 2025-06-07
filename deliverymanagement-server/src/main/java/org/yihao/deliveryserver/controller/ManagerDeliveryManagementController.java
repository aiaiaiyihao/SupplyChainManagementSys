package org.yihao.deliveryserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.deliveryserver.Exception.APIException;
import org.yihao.deliveryserver.service.DeliverymanagementService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;

/*kafka要监听 有批准订单就会把订单dto传过来
 选择骑手 然后根据订单factoryId和warehouseId 自动生成delivery
 deliveryResponse需要包含地址信息
 生成delivery后 邮件通知 骑手和经理(kafka)取货地址（根据factoryId查询），
 预期时间，投放仓库地址（根据warehouseId查询） 产品名称 产品数量
  骑手也可以登录系统 查看自己分配的运单信息 和取货投放地址 产品名称数量
  然后骑手可以自行更新deliverystatus为(投递中)
  运到后 更新deliverystatus 后系统自动更新orderstatus
  自动更新invertory和InBound/OutBound
 */

@RestController
@RequestMapping("/admin/deliverymanagement")
public class ManagerDeliveryManagementController {
    private final DeliverymanagementService deliverymanagementService;

    public ManagerDeliveryManagementController(DeliverymanagementService deliverymanagementService) {
        this.deliverymanagementService = deliverymanagementService;
    }

    //button in order system to generate delivery
    @PostMapping
    public ResponseEntity<DeliveryDTO> addDelivery(
            @RequestHeader("role") String role,
            @RequestBody CreateDeliveryRequest request) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Generate Delivery By Order");
        }
        DeliveryDTO deliveryCreated = deliverymanagementService.addDelivery(request);
        return ResponseEntity.ok(deliveryCreated);
    }


}
