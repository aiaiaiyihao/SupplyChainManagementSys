package org.yihao.invitationserver.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.OrderDTO;
import org.yihao.shared.ENUMS.OrderStatus;
import org.yihao.shared.config.AppConstants;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Slf4j
public class OrderGenerationRemindedService {
    private final EmailService emailService;

    public OrderGenerationRemindedService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME_ORDER, groupId = "auditing-group")
    public void consume(OrderDTO orderDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a",Locale.ENGLISH);
        String subject;
        String body;
        if(orderDTO.getOrderStatus().equals(OrderStatus.CREATED)){
            subject = "Order No. " + orderDTO.getOrderId() + " generated, please review";

            body = String.format("""
            Dear %s,

            A new order has been created in our system.

            Order Details:
            - Order ID: %d
            - Product: %s
            - Quantity: %d
            - Price per Unit: $%.2f
            - Total Price: $%.2f
            - Currency: %s
            - Order Status: %s
            - Created At: %s

            Please review the order and prepare for fulfillment.

            Regards,
            Supply Chain Management System
            """,
                    orderDTO.getSupplierName(),
                    orderDTO.getOrderId(),
                    orderDTO.getProductName(),
                    orderDTO.getProductQuantity(),
                    orderDTO.getPerPrice(),
                    orderDTO.getTotalPrice(),
                    orderDTO.getCurrency(),
                    orderDTO.getOrderStatus(),
                    orderDTO.getCreateAt().format(formatter)
            );
        } else{
            subject = "Order No. " + orderDTO.getOrderId() + " return requested, please review";

            body = String.format("""
            Dear %s,

            Order No. %d has been requested to return in our system;

            Order Details:
            - Product: %s
            - Quantity: %d
            - Price per Unit: $%.2f
            - Total Price: $%.2f
            - Currency: %s
            - Order Status: %s
            - Created At: %s

            Please review the order and prepare for fulfillment.

            Regards,
            Supply Chain Management System
            """,
                    orderDTO.getSupplierName(),
                    orderDTO.getOrderId(),
                    orderDTO.getProductName(),
                    orderDTO.getProductQuantity(),
                    orderDTO.getPerPrice(),
                    orderDTO.getTotalPrice(),
                    orderDTO.getCurrency(),
                    orderDTO.getOrderStatus(),
                    orderDTO.getCreateAt().format(formatter)
            );
        }


        emailService.sendEmail(orderDTO.getEmail(), subject, body);
        log.info("email sent");
    }




}
