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
public class OrderApprovalReminderService {

    private final EmailService emailService;
    public OrderApprovalReminderService(EmailService emailService) {
        this.emailService = emailService;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.ENGLISH);

    @KafkaListener(topics = AppConstants.TOPIC_NAME_ORDERAPPROVAL, groupId = "auditing-group")
    public void handleApproval(OrderDTO orderDTO){
        String subject = "Order No. " + orderDTO.getOrderId() + " approved, please review";
        boolean isReturn = orderDTO.getOrderStatus().equals(OrderStatus.RETURNAPPROVED);
        String body = buildEmailBody(orderDTO,true,isReturn);

        emailService.sendEmail(AppConstants.ADMINEMAIL, subject, body);
        log.info("email sent");
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME_ORDERDENIAL, groupId = "auditing-group")
    public void handleDenial(OrderDTO orderDTO){
        String subject = "Order No. " + orderDTO.getOrderId() + " denied, please check the reason";
        boolean isReturn = orderDTO.getOrderStatus().equals(OrderStatus.RETURNAPPROVED);
        String body = buildEmailBody(orderDTO,false,isReturn);

        emailService.sendEmail(AppConstants.ADMINEMAIL, subject, body);
        log.info("email sent");
    }

    private String buildEmailBody(OrderDTO order, boolean isApproved,boolean isReturn) {
        String status = isApproved ? "approved" : "denied";
        String isreturn = isReturn ? "return": "purchasing";

        return String.format("""
        Dear Purchasing Manager,

        Your %s order has been %s in our system.

        Order Details:
        - Order ID: %d
        - Product: %s
        - Quantity: %d
        - Price per Unit: $%.2f
        - Total Price: $%.2f
        - Currency: %s
        - Order Status: %s
        - Created At: %s
        - message: %s
        

        Regards,
        %s
        """,
                isreturn,
                status,
                order.getOrderId(),
                order.getProductName(),
                order.getProductQuantity(),
                order.getPerPrice(),
                order.getTotalPrice(),
                order.getCurrency(),
                order.getOrderStatus(),
                order.getCreateAt().format(formatter),
                order.getMessage(),
                order.getSupplierName()
        );
    }

}
