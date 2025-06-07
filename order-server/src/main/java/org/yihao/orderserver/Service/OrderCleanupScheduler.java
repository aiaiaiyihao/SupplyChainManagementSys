package org.yihao.orderserver.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yihao.orderserver.Model.Order;
import org.yihao.orderserver.Repository.OrderRepository;
import org.yihao.shared.ENUMS.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/*After two weeks, held order will be auto cancelled*/
@Service
public class OrderCleanupScheduler {
    private final OrderRepository orderRepository;

    public OrderCleanupScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // Every day at 2 AM
    public void autoCancelHeldOrders() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        List<Order> heldOrders = orderRepository.findByOrderStatusAndUpdateAtBefore(
                OrderStatus.HELD, twoWeeksAgo
        );

        for (Order order : heldOrders) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setUpdateAt(LocalDateTime.now());
            orderRepository.save(order);
            System.out.println("Auto-cancelled order: " + order.getOrderId());
        }

    }
}
