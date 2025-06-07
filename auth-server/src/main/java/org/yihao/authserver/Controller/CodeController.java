package org.yihao.authserver.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yihao.authserver.Service.CodeService;
import org.yihao.shared.DTOS.VerificationRequest;
import org.yihao.shared.DTOS.VerificationResponse;

@RestController
@RequestMapping("/auth")
public class CodeController {
    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("verification")
    public ResponseEntity<VerificationResponse> generateVerificationCode(@RequestBody VerificationRequest verificationRequest) {
        VerificationResponse response = codeService.generateVerificationCode(verificationRequest);
        return ResponseEntity.ok(response);
    }

}
