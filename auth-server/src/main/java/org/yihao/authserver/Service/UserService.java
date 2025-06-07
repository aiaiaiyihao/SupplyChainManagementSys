package org.yihao.authserver.Service;

import jakarta.validation.Valid;
import org.yihao.authserver.DTO.LoginRequest;
import org.yihao.authserver.DTO.LoginResponse;
import org.yihao.authserver.DTO.SignupDriverRequest;
import org.yihao.authserver.DTO.SignupSupplierRequest;

public interface UserService {
    LoginResponse authenticateUser(@Valid LoginRequest loginRequest);

    void registerDriver(@Valid SignupDriverRequest signupDriverRequest);

    void registerSupplier(@Valid SignupSupplierRequest signupSupplierRequest);
}

