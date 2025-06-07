package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.Role;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    private String email;
    private Role role;
}
