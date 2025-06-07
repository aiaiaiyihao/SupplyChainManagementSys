package org.yihao.invitationserver.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.Address;
import org.yihao.shared.DTOS.DeliveryDTO;
import org.yihao.shared.DTOS.DeliveryNotificationDTO;
import org.yihao.shared.config.AppConstants;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Slf4j
public class DeliveryGenerationReminderService {
    private final EmailService emailService;

    public DeliveryGenerationReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME_DELIVERY, groupId = "auditing-group")
    public void deliveryNotify(DeliveryNotificationDTO deliveryNotificationDTO){
        log.info("Received deliveryNotificationDTO: " + deliveryNotificationDTO);
        DeliveryDTO deliveryDTO = deliveryNotificationDTO.getDeliveryDTO();
        String deliveryType = deliveryDTO.getDeliveryType().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String subjectSupplier = "Delivery No."+deliveryDTO.getDeliveryId()+" generated, please review";
        String bodySupplier = String.format("""
            Dear %s,

            Your delivery for order No.%s has been generated in our system.

            Delivery Details:
            - Product Name: %s
            - Product Quantity: %s
            - Driver Name: %s
            - Driver Number: %s
            - DeliveryType: %s
            - DeliveryStatus: %s
            - Created At: %s
            - EstimatedDeliveryTime: %s
            - %s Address: %s

            Please review the %s delivery and prepare for the %s.

            Regards,
            SupplyChain Management System
            """,
                deliveryNotificationDTO.getSupplierName(),
                deliveryDTO.getOrderId(),
                deliveryNotificationDTO.getProductName(),
                deliveryNotificationDTO.getProductQuantity(),
                deliveryNotificationDTO.getDriverName(),
                deliveryNotificationDTO.getDriverNumber(),
                deliveryDTO.getDeliveryType(),
                deliveryDTO.getDeliveryStatus(),
                deliveryDTO.getCreatedAt().format(formatter),
                deliveryDTO.getEstimatedDeliveryTime().format(formatter),
                deliveryType.toLowerCase(),
                formatAddress(deliveryNotificationDTO.getPickupAddress()),
                deliveryType.toLowerCase(),
                deliveryType.toLowerCase()
        );

        String subjectDriver = "Delivery No."+deliveryDTO.getDeliveryId()+" generated, please deliver";
        String bodyDriver = String.format("""
            Dear %s,

            You have been assigned %s for order No.%s.

            Delivery Details:
            - Product Name: %s
            - Product Quantity: %s
            - Driver ID: %s
            - Driver Name: %s
            - Driver Number: %s
            - DeliveryType: %s
            - DeliveryStatus: %s
            - Created At: %s
            - Delivery Deadline: %s
            - %s Address: %s
            - InventoryAddress: %s

            Please review the %s delivery and prepare for %s.

            Regards,
            SupplyChain Management System
            """,
                deliveryNotificationDTO.getDriverName(),
                deliveryType.toLowerCase(),
                deliveryDTO.getOrderId(),
                deliveryNotificationDTO.getProductName(),
                deliveryNotificationDTO.getProductQuantity(),
                deliveryDTO.getDriverId(),
                deliveryNotificationDTO.getDriverName(),
                deliveryNotificationDTO.getDriverNumber(),
                deliveryDTO.getDeliveryType(),
                deliveryDTO.getDeliveryStatus(),
                deliveryDTO.getCreatedAt().format(formatter),
                deliveryDTO.getEstimatedDeliveryTime().format(formatter),
                deliveryType.toLowerCase(),
                formatAddress(deliveryNotificationDTO.getPickupAddress()),
                formatAddress(deliveryNotificationDTO.getInventoryAddress()),
                deliveryType.toLowerCase(),
                deliveryType.toLowerCase()
        );

        emailService.sendEmail(deliveryNotificationDTO.getSupplierEmail(), subjectSupplier, bodySupplier);
        emailService.sendEmail(deliveryNotificationDTO.getDriverEmail(),subjectDriver,bodyDriver);
        log.info("email sent");

    }

    private String formatAddress(Address address) {
        return String.format("%s%s, %s, %s %d, %s",
                address.getLine1(),
                address.getLine2() != null ? ", " + address.getLine2() : "",
                address.getCity(),
                address.getState(),
                address.getZip(),
                address.getCountry());
    }

}
