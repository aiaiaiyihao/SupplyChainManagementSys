package org.yihao.authserver.Service;

import org.yihao.shared.DTOS.VerificationRequest;
import org.yihao.shared.DTOS.VerificationResponse;

public interface CodeService {
    VerificationResponse generateVerificationCode(VerificationRequest verificationRequest);
}
