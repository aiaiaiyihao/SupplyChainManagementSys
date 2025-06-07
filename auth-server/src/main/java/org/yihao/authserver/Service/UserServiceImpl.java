package org.yihao.authserver.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yihao.authserver.DTO.LoginRequest;
import org.yihao.authserver.DTO.LoginResponse;
import org.yihao.authserver.DTO.SignupDriverRequest;
import org.yihao.authserver.DTO.SignupSupplierRequest;
import org.yihao.authserver.Enum.Role;
import org.yihao.authserver.Exception.APIException;
import org.yihao.authserver.Secutiry.JwtUtils;
import org.yihao.authserver.Secutiry.Service.UserDetailsImpl;
import org.yihao.authserver.Mode.User;
import org.yihao.authserver.Repository.UserRepository;
import org.yihao.authserver.Service.Remote.RemoteDriverService;
import org.yihao.authserver.Service.Remote.RemoteSupplierService;
import org.yihao.shared.DTOS.DriverDTO;
import org.yihao.shared.DTOS.SupplierDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,String> redisTemplate;
    private final RemoteSupplierService remoteSupplierService;
    private final RemoteDriverService remoteDriverService;

    public UserServiceImpl(JwtUtils jwtUtils, AuthenticationManager authenticationManager,
                           UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RedisTemplate<String, String> redisTemplate,
                           RemoteSupplierService remoteSupplierService,
                           RemoteDriverService remoteDriverService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.remoteSupplierService = remoteSupplierService;
        this.remoteDriverService = remoteDriverService;
    }


    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails =(UserDetailsImpl) authentication.getPrincipal();

        //1 for jwt in header
        String jwtToken = jwtUtils.generateTokenFromUser(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());

        return new LoginResponse(userDetails.getId(),jwtToken, userDetails.getUsername(), roles);
    }

    @Override
    public void registerDriver(SignupDriverRequest signupDriverRequest) {
        String verificationCode = signupDriverRequest.getVerificationCode();
        String role = redisTemplate.opsForValue().get(verificationCode);
//        Optional<VerificationCode> codeStored = codeRepository.findByCode(verificationCode);
        if(role==null){
            throw new APIException("Error: Verification Code invalid");
        }
        Role r = Role.valueOf(role);


        if(userRepository.existsByUserName(signupDriverRequest.getUsername())){
            throw new APIException("Error: Username already exists");
        }
        if(userRepository.existsByEmail(signupDriverRequest.getEmail())){
            throw new APIException("Error: Email already exists");
        }

        User newUser = new User(signupDriverRequest.getUsername(), signupDriverRequest.getEmail()
                , passwordEncoder.encode(signupDriverRequest.getPassword()), r);
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(newUser.getEmail());
        driverDTO.setFirstName(signupDriverRequest.getFirstName());
        driverDTO.setLastName(signupDriverRequest.getLastName());
        driverDTO.setPhoneNumber(signupDriverRequest.getPhoneNumber());
        driverDTO.setDateOfBirth(signupDriverRequest.getDateOfBirth());
        driverDTO.setLicenseNumber(signupDriverRequest.getLicenseNumber());
        driverDTO.setLicenseExpiryDate(signupDriverRequest.getLicenseExpiryDate());
        log.info(driverDTO.toString());
        DriverDTO created= remoteDriverService.createDriver(Role.MANAGER.name(), driverDTO).getBody();
        if(created==null) throw new APIException("Error: Driver creation failed");
        newUser.setTableId(created.getDriverId());

        userRepository.save(newUser);
    }

    @Override
    public void registerSupplier(SignupSupplierRequest request) {
        String verificationCode = request.getVerificationCode();
        String role = redisTemplate.opsForValue().get(verificationCode);
//        Optional<VerificationCode> codeStored = codeRepository.findByCode(verificationCode);
        if(role==null){
            throw new APIException("Error: Verification Code invalid");
        }
        Role r = Role.valueOf(role);


        if(userRepository.existsByUserName(request.getUsername())){
            throw new APIException("Error: Username already exists");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new APIException("Error: Email already exists");
        }

        User newUser;
        newUser = new User(request.getUsername(), request.getEmail()
                , passwordEncoder.encode(request.getPassword()), r);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setEmail(request.getEmail());
        supplierDTO.setPhoneNumber(request.getPhoneNumber());
        supplierDTO.setSupplierName(request.getSupplierName());
        supplierDTO.setAddress(request.getAddress());
        supplierDTO.setWebPage(request.getWebPage());
        supplierDTO.setParentCompany(request.getParentCompany());
        supplierDTO.setIndustryType(request.getIndustryType());
        supplierDTO.setYearEstablished(request.getYearEstablished());
        supplierDTO.setDocuments(request.getDocuments());
        SupplierDTO supplierCreated = remoteSupplierService.createSupplier(Role.MANAGER.name(), supplierDTO).getBody();
        log.info("suppliercreated: "+supplierCreated.toString());
        if(supplierCreated==null) throw new APIException("Error: Supplier creation failed");
        newUser.setTableId(supplierCreated.getSupplierId());
        userRepository.save(newUser);
    }
}
