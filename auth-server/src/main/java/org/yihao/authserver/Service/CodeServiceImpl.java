package org.yihao.authserver.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yihao.authserver.Exception.APIException;
import org.yihao.authserver.Util.VerificationCodeGenerator;
import org.yihao.shared.DTOS.VerificationRequest;
import org.yihao.shared.DTOS.VerificationResponse;
import org.yihao.shared.ENUMS.Role;

import java.time.Duration;

@Service
public class CodeServiceImpl implements CodeService {
    private final RedisTemplate<String,String> redisTemplate;

    public CodeServiceImpl(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public VerificationResponse generateVerificationCode(VerificationRequest verificationRequest) {
        //generate code here
        VerificationResponse verificationResponse = new VerificationResponse();
        try {
//            String email = verificationRequest.getEmail();
            Role role = verificationRequest.getRole();
            String code = VerificationCodeGenerator.generateCode();
            verificationResponse.setRole(verificationRequest.getRole());
            verificationResponse.setCode(code);
            redisTemplate.opsForValue().set(code, role.name(), Duration.ofMinutes(30));
        } catch (Exception e) {
            throw new APIException(e.getMessage());
        }

//        LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);

//        VerificationCode saved = codeRepository.save(verificationCode);

        return verificationResponse;
    }
}
