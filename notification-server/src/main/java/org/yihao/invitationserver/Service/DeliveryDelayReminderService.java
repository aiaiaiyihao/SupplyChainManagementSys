package org.yihao.invitationserver.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.DeliveryDelayDTO;
import org.yihao.shared.DTOS.DeliveryNotificationDTO;
import org.yihao.shared.config.AppConstants;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Slf4j
public class DeliveryDelayReminderService {
    private final EmailService emailService;
    public DeliveryDelayReminderService(EmailService emailService) {
        this.emailService = emailService;
    }
    @KafkaListener(topics = AppConstants.TOPIC_NAME_DELIVERY_DELAY, groupId = "auditing-group")
    public void deliveryDelayNotify(DeliveryDelayDTO deliveryDelayDTO){
        log.info("Received deliveryDelayDTO: " + deliveryDelayDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String subjectSupplier = "Delivery No."+deliveryDelayDTO.getDeliveryId()+" is delayed, please review";
        String bodySupplier = String.format("""
            Dear %s,

                Your delivery for deliveryId: %s was delayed.
                
                The estimated delivery time is: %s
    
                Please delivery soon.
            
            Regards,
            SupplyChain Management System
            """,
                deliveryDelayDTO.getDriverName(),
                deliveryDelayDTO.getDeliveryId(),
                deliveryDelayDTO.getEstimatedDeliveryTime().format(formatter)
        );

        String subjectManager = "Delivery No."+deliveryDelayDTO.getDeliveryId()+" is delayed, please review";
        String bodyManager = String.format("""
            Dear Manager,

                Your delivery for deliveryId: %s was delayed.
                
                The delivery was assigned to: %s
                
                His driverId is: %s
                
                The estimated delivery time is: %s
    
                Please review soon.
            
            Regards,
            SupplyChain Management System
            """,
                deliveryDelayDTO.getDeliveryId(),
                deliveryDelayDTO.getDriverName(),
                deliveryDelayDTO.getDriverId(),
                deliveryDelayDTO.getEstimatedDeliveryTime().format(formatter)
        );
        emailService.sendEmail(deliveryDelayDTO.getDriveEmail(), subjectSupplier, bodySupplier);
        log.info("email sent to driver");
        emailService.sendEmail(AppConstants.ADMINEMAIL, subjectManager, bodyManager);
        log.info("email sent to admin");

    }

    @KafkaListener(topics=AppConstants.TOPIC_NAME_DELIVERY_DELAY_DRIVER, groupId = "auditing-group")
    public void deliveryDeliveriedDelayNotify(DeliveryNotificationDTO deliveryNotificationDTO){
        log.info("Received deliveryNotificationDTO: " + deliveryNotificationDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        Long deliveryId = deliveryNotificationDTO.getDeliveryDTO().getDeliveryId();
        String subjectSupplier = "Delivery No."+deliveryId+" is delivered but delayed, please review";
        String bodySupplier = String.format("""
            Dear %s,

                Your delivery for deliveryId: %s was delayed.
                
                The estimated delivery time is: %s
    
                The delivery was deliveried at: %s
            
            Regards,
            SupplyChain Management System
            """,
                deliveryNotificationDTO.getDriverName(),
                deliveryId,
                deliveryNotificationDTO.getDeliveryDTO().getEstimatedDeliveryTime().format(formatter),
                deliveryNotificationDTO.getDeliveryDTO().getUpdatedAt().format(formatter)
        );
        emailService.sendEmail(deliveryNotificationDTO.getDriverEmail(), subjectSupplier, bodySupplier);
        log.info("email sent to driver");
    }
}
