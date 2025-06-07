package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationNotificationRequest {
    private String name;
    private String email;
    private VerificationResponse verificationResponse;
}
