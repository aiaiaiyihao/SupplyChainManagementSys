package org.yihao.authserver.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.yihao.authserver.DTO.*;
import org.yihao.authserver.Exception.APIException;
import org.yihao.authserver.Service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            LoginResponse loginResponse = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is disabled");
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("User account is locked");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/signup/driver")
    public ResponseEntity<?> signUpDriver(@Valid @RequestBody SignupDriverRequest signupDriverRequest){
        try {
            userService.registerDriver(signupDriverRequest);
            return new ResponseEntity<>(new MessageResponse("User registered successfully!"),HttpStatus.CREATED);
        } catch (APIException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }
    }

    @PostMapping("/signup/supplier")
    public ResponseEntity<?> signUpDriver(@Valid @RequestBody SignupSupplierRequest signupSupplierRequest){
        try {
            userService.registerSupplier(signupSupplierRequest);
            return new ResponseEntity<>(new MessageResponse("User registered successfully!"),HttpStatus.CREATED);
        } catch (APIException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }
    }
}
