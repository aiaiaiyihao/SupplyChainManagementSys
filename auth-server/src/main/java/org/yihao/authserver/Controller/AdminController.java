package org.yihao.authserver.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yihao.authserver.DTO.SignupAdminRequest;
import org.yihao.authserver.DTO.SignupDriverRequest;
import org.yihao.authserver.Service.AdminService;

@RestController
@RequestMapping("/auth")
public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/admin/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody SignupAdminRequest signupAdminRequest) {
        adminService.registerAdmin(signupAdminRequest);
        return ResponseEntity.ok("Admin register success");
    }
}
