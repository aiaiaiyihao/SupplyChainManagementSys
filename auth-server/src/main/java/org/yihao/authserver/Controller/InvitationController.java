package org.yihao.authserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yihao.authserver.Service.InvitationService;

@RestController
@RequestMapping("/auth/admin/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("supplier")
    public ResponseEntity<String> inviteSupplier(@RequestParam(name="email") String email
            , @RequestParam(name="name") String name) {
        Boolean inviteResult = invitationService.inviteSupplier(email,name);
        if (inviteResult) {return new ResponseEntity<>("invitation sent success", HttpStatus.OK);}
        else return new ResponseEntity<>("invitation not sent", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("driver")
    public ResponseEntity<String> inviteDriver(@RequestParam(name="email") String email
            , @RequestParam(name="name") String name) {
        Boolean inviteResult = invitationService.inviteDriver(email,name);
        if (inviteResult) {return new ResponseEntity<>("invitation sent success", HttpStatus.OK);}
        else return new ResponseEntity<>("invitation not sent", HttpStatus.BAD_REQUEST);
    }
}

