package org.yihao.authserver.Util;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class VerificationCodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        int code = 10000 + random.nextInt(90000); // Range: 10000–99999
        return String.valueOf(code);
    }
}
