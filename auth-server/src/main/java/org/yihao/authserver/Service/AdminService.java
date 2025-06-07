package org.yihao.authserver.Service;

import org.yihao.authserver.DTO.SignupAdminRequest;
import org.yihao.authserver.DTO.SignupDriverRequest;

public interface AdminService {
    void registerAdmin(SignupAdminRequest signupAdminRequest);
}
