package org.yihao.invitationserver.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.VerificationNotificationRequest;
import org.yihao.shared.DTOS.VerificationResponse;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

import java.util.Objects;

@Service
@Slf4j
public class InvitationReminderService {
    private final EmailService emailService;
    public InvitationReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME_INVITATION, groupId = "auditing-group")
    public void inviteEmail(VerificationNotificationRequest verificationNotificationRequest) {
        String name = verificationNotificationRequest.getName();
        String email = verificationNotificationRequest.getEmail();
        VerificationResponse verificationResponse = verificationNotificationRequest.getVerificationResponse();
        String role = verificationResponse.getRole().equals(Role.DRIVER)?"driver":"supplier";
        String body = String.format("""
        Dear %s,

        We are excited to invite you to join our platform as a %s.

        To complete your registration, please use the verification code below. 
        This code will expire in 30 minutes.

        ------------------------------
        Verification Code: %s
        ------------------------------

        Welcome aboard, and we look forward to working with you!

        Best regards,
        SupplyChain Management Team
        """,
                name,
                role,
                Objects.requireNonNull(verificationResponse).getCode()
        );

        emailService.sendEmail(
                email,
                "You're invited to join us, " + name,
                body
        );
    }
}
