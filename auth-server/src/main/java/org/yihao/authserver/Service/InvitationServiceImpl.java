package org.yihao.authserver.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.*;
import org.yihao.shared.config.AppConstants;


import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {
    private CodeService codeService;
    private KafkaTemplate<String, VerificationNotificationRequest> kafkaTemplate;

    public InvitationServiceImpl(CodeService codeService, KafkaTemplate<String, VerificationNotificationRequest> kafkaTemplate) {
        this.codeService = codeService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Boolean inviteSupplier(String email, String name) {
        VerificationRequest verificationRequest= new VerificationRequest();
        verificationRequest.setRole(Role.SUPPLIER);
        VerificationResponse verificationResponse = codeService.generateVerificationCode(verificationRequest);
        VerificationNotificationRequest verificationNotificationRequest = new VerificationNotificationRequest();
        verificationNotificationRequest.setEmail(email);
        verificationNotificationRequest.setName(name);
        verificationNotificationRequest.setVerificationResponse(verificationResponse);
//        boolean result = emailService.sendEmail(email, "Invitation to be part of us: " + name
//                , "Welcome to be One of our business partner: " + name+".\n\n"+
//                        " here is your verification code for registration. It will expire in 30 mins."+".\n\n"
//                        + Objects.requireNonNull(verificationResponse).getCode()
//        );
        return generateEmail(verificationNotificationRequest);
    }

    @Override
    public Boolean inviteDriver(String email, String name) {
        VerificationRequest verificationRequest = new VerificationRequest();
        verificationRequest.setEmail(email);
        verificationRequest.setRole(Role.DRIVER);
        VerificationResponse verificationResponse = codeService.generateVerificationCode(verificationRequest);
        VerificationNotificationRequest verificationNotificationRequest = new VerificationNotificationRequest();
        verificationNotificationRequest.setEmail(email);
        verificationNotificationRequest.setName(name);
        verificationNotificationRequest.setVerificationResponse(verificationResponse);

//        boolean result = emailService.sendEmail(email, "Invitation to be part of us: " + name
//                , "Welcome to be One of our driver: " + name+".\n\n"+
//                        " here is your verification code for registration. It will expire in 30 mins."+".\n\n"
//                        + Objects.requireNonNull(verificationResponse).getCode()
//        );
        return generateEmail(verificationNotificationRequest);
    }

    private boolean generateEmail(VerificationNotificationRequest verificationNotificationRequest) {
        CompletableFuture<SendResult<String, VerificationNotificationRequest>> future =
                kafkaTemplate.send(AppConstants.TOPIC_NAME_INVITATION, verificationNotificationRequest);
        future.whenComplete((res, err) -> {
            if (Objects.isNull(err)) {
                log.info("send message=[{}] with offset=[{}]",
                        verificationNotificationRequest.toString(), res.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[{}] due to: {}",
                        verificationNotificationRequest.toString(), err.getMessage());
            }
        });
        return true;
    }
}
