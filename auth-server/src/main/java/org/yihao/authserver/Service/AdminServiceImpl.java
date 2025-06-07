package org.yihao.authserver.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yihao.authserver.DTO.SignupAdminRequest;
import org.yihao.authserver.Enum.Role;
import org.yihao.authserver.Exception.APIException;
import org.yihao.authserver.Mode.User;
import org.yihao.authserver.Repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {
    @Value("${spring.app.adminRegistrationCode}")
    private String adminRegistrationCode;

    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerAdmin(SignupAdminRequest request) {
        if(!request.getVerificationCode().equals(adminRegistrationCode)){
            throw new APIException("Verification code isn't incorrect");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new APIException("Email address already in use");
        }
        if(userRepository.existsByUserName(request.getUsername())){
            throw new APIException("Username already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserName(request.getUsername());
        user.setRole(Role.MANAGER);
        userRepository.save(user);
    }
}
