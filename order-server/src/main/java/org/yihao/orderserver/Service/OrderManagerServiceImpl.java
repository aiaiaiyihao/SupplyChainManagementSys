package org.yihao.orderserver.Service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.yihao.orderserver.Exception.APIException;
import org.yihao.orderserver.Exception.ResourceNotFoundException;
import org.yihao.orderserver.Model.Order;
import org.yihao.orderserver.Repository.OrderRepository;
import org.yihao.orderserver.Specification.OrderSpecification;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderManagerServiceImpl implements OrderManagerService{
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderManagerServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponse getAllOrders(Long productId, String productName, Long supplierId, String supplierName, OrderStatus orderStatus, Long deliveryId,
                                      Long factoryId, Long warehouseId, Integer pageNumber, Integer pageSize, String sortBy, boolean desc) {
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Order> specification = Specification.where(OrderSpecification.hasProductId(productId)).and(OrderSpecification.hasProductNameLike(productName))
                .and(OrderSpecification.hasSupplierId(supplierId)).and(OrderSpecification.hasSupplierNameLike(supplierName))
                .and(OrderSpecification.hasOrderStatus(orderStatus)).and(OrderSpecification.hasDeliveryId(deliveryId))
                .and(OrderSpecification.hasFactoryId(factoryId)).and(OrderSpecification.hasWarehouseId(warehouseId));
        Page<Order> ordersInPage = orderRepository.findAll(specification,pageDetails);
        List<Order> orders = ordersInPage.getContent();
        if(orders.isEmpty()) throw new APIException("No orders found");
        List<OrderDTO> orderDTOS = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class)).toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOS);
        orderResponse.setPageNumber(ordersInPage.getNumber());
        orderResponse.setPageSize(ordersInPage.getSize());
        orderResponse.setTotalPages(ordersInPage.getTotalPages());
        orderResponse.setTotalElements(ordersInPage.getTotalElements());
        orderResponse.setLastPage(ordersInPage.isLast());

        return orderResponse;
    }



    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    @CachePut(value = "orders", key = "#result.orderId")
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setPerPrice(orderDTO.getPerPrice());
        order.setTotalPrice(order.getPerPrice()
                .multiply(BigDecimal.valueOf(order.getProductQuantity())));
        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CREATED);
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    /*When you use @CachePut(key = "#result.id"), #result refers to the return value of the method.*/
    @CachePut(value = "orders", key = "#result.orderId")
    public OrderDTO cloneOrder(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        Order clone = new Order();

        /*This line tells ModelMapper to:
    🔁 Copy all matching properties from the order object into the already-created clone object.
    It's like saying: “Take the values from order and copy them into clone,
    but don’t create a new object, just fill in the existing one.”*/
        modelMapper.map(order, clone);
        clone.setOrderId(null);
        clone.setCreateAt(LocalDateTime.now());
        clone.setUpdateAt(LocalDateTime.now());
        clone.setOrderStatus(OrderStatus.CREATED);
        Order saved = orderRepository.save(clone);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    @CachePut(value = "orders", key = "#id")
    public OrderDTO updateById(Long id, OrderDTO orderDTO) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
//        OrderStatus orderStatus = order.getOrderStatus();
//        if(orderStatus!=OrderStatus.CREATED) throw new APIException("Order status is "+orderStatus.name()+". It is not changable");
        if (orderDTO.getCurrency() != null) {
            order.setCurrency(orderDTO.getCurrency());
        }

        if (order.getPerPrice() != null && orderDTO.getProductQuantity() != null) {
            order.setTotalPrice(order.getPerPrice()
                    .multiply(BigDecimal.valueOf(orderDTO.getProductQuantity())));
        }

        if (order.getPerPrice() != null) {
            order.setPerPrice(order.getPerPrice()); // This line looks redundant, consider removing it
        }

        if (orderDTO.getProductId() != null) {
            order.setProductId(orderDTO.getProductId());
        }

        if (orderDTO.getProductName() != null) {
            order.setProductName(orderDTO.getProductName());
        }

        if (orderDTO.getProductQuantity() != null) {
            order.setProductQuantity(orderDTO.getProductQuantity());
        }

        if (orderDTO.getSupplierId() != null) {
            order.setSupplierId(orderDTO.getSupplierId());
        }

        if (orderDTO.getSupplierName() != null) {
            order.setSupplierName(orderDTO.getSupplierName());
        }
        if(orderDTO.getWarehouseId()!=null) {
            order.setWarehouseId(orderDTO.getWarehouseId());
        }

        if(orderDTO.getFactoryId()!=null) {
            order.setFactoryId(orderDTO.getFactoryId());
        }
        order.setUpdateAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    @CachePut(value = "orders", key = "#id")
    public OrderDTO holdOrder(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        order.setOrderStatus(OrderStatus.HELD);
        order.setUpdateAt(LocalDateTime.now());
        Order heldOrder = orderRepository.save(order);
        return modelMapper.map(heldOrder, OrderDTO.class);
    }



    @Override
    @CachePut(value = "orders", key = "#id")
    public OrderDTO cancelOrder(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        if(order.getOrderStatus()==OrderStatus.DELIVERING){
            throw new APIException("Order is already processing, cannot cancel order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdateAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    @CachePut(value = "orders", key = "#request.orderId")
    public OrderDTO changeOrderStatus(OrderStatusUpdateRequest request) {
        Long id = request.getOrderId();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        order.setOrderStatus(request.getStatus());
        order.setDeliveryId(request.getDeliveryId());
        order.setUpdateAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    @CachePut(value = "orders", key = "#id")
    public OrderDTO returnOrder(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        if(order.getOrderStatus()!=OrderStatus.DELIVERED){
            throw new APIException("Order Status has to be DELIVERED to request return");
        }
        order.setOrderStatus(OrderStatus.RETURNREQUESTED);
        order.setUpdateAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public OrderDTO deleteById(Long id) {
        log.info("Fetching order from DB with id {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", "orderId", id));
        if(order.getOrderStatus()!=OrderStatus.CANCELLED){
            throw new APIException("You have to cancel order before deleting this order");
        }
        orderRepository.deleteById(id);
        return modelMapper.map(order, OrderDTO.class);
    }




}
