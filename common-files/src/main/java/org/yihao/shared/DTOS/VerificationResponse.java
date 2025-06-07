package org.yihao.shared.DTOS;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.Role;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VerificationResponse {
    private String code;
    private LocalDateTime expireTime;
    @Enumerated(EnumType.STRING)
    private Role role;
}
