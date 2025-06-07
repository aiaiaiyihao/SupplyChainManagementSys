package org.yihao.orderserver.Service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yihao.orderserver.Exception.APIException;
import org.yihao.orderserver.Exception.ResourceNotFoundException;
import org.yihao.orderserver.Model.Order;
import org.yihao.orderserver.Repository.OrderRepository;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderSupplierServiceImpl implements OrderSupplierService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderSupplierServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponse getOrderBySupplierId(Long supplierId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Order> ordersInPage = orderRepository.findBySupplierId(supplierId, pageDetails);
        List<Order> orders = ordersInPage.getContent();
        if (orders.isEmpty()) throw new APIException("No orders found");
        List<OrderDTO> orderDTOS = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class)).toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOS);
        orderResponse.setPageNumber(ordersInPage.getNumber());
        orderResponse.setPageSize(ordersInPage.getSize());
        orderResponse.setTotalPages(ordersInPage.getTotalPages());
        orderResponse.setTotalElements(ordersInPage.getTotalElements());
        return orderResponse;
    }

    @Override
    @CachePut(value = "orders", key = "#approval.orderId")
    public OrderDTO approveByOrderId(Long supplierId, OrderApprovalDTO approval) {
        Long orderId = approval.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", orderId));
        if (!order.getSupplierId().equals(supplierId)) {
            throw new APIException("Sorry,this order doesn't belong to you.");
        }
        if(order.getOrderStatus() != OrderStatus.CREATED
                && order.getOrderStatus() != OrderStatus.RETURNREQUESTED) {
            throw new APIException("You can't approve this order");
        }
        if(order.getOrderStatus() == OrderStatus.CREATED) {
            if (approval.getApprovalResult()) {
                order.setOrderStatus(OrderStatus.APPROVED);

            } else {
                order.setOrderStatus(OrderStatus.REJECTED);
            }
        } else{
            if(approval.getApprovalResult()) {
                order.setOrderStatus(OrderStatus.RETURNAPPROVED);
            } else{
                order.setOrderStatus(OrderStatus.RETURNREJECTED);
            }
        }
        if(approval.getFactoryId()!=null) {
            order.setFactoryId(approval.getFactoryId());
        }

        order.setUpdateAt(LocalDateTime.now());
        Order saveOrder = orderRepository.save(order);
        return modelMapper.map(saveOrder, OrderDTO.class);
    }
}
