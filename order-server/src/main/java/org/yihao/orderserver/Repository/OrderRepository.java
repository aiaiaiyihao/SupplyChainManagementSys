package org.yihao.orderserver.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.orderserver.Model.Order;
import org.yihao.shared.ENUMS.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndUpdateAtBefore(OrderStatus orderStatus, LocalDateTime updateAtBefore);


    Page<Order> findBySupplierId(Long supplierId, Pageable pageDetails);

    Page<Order> findAll(Specification<Order> specification, Pageable pageDetails);

}

