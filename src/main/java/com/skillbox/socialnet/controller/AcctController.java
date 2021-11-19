package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.service.AcctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Semen V
 * @created 18|11|2021
 */

@RestController
@RequestMapping("/api/v1/account")
public class AcctController {

    @Autowired
    private AcctService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AcctRegisterRQ acctRegisterRQ) {
        return ResponseEntity.ok(accountService.register());
    }

    @PutMapping("/password/recovery")
    public ResponseEntity<?> passwordRecovery(@RequestBody AcctEmailRQ acctEmailRequest) {
        return ResponseEntity.ok(accountService.passwordRecovery());
    }

    @PutMapping("/password/set")
    public ResponseEntity<?> passwordSet(@RequestBody AcctPasswordSetRQ acctPasswordSetRQ) {
        return ResponseEntity.ok(accountService.passwordRecovery());
    }

    @PutMapping("/email")
    public ResponseEntity<?> email(@RequestBody AcctEmailRQ acctEmailRequest) {
        return ResponseEntity.ok(accountService.passwordRecovery());
    }

    @PutMapping("/password/notifications")
    public ResponseEntity<?> notifications(@RequestBody AcctNotificationRQ acctNotificationRQ) {
        return ResponseEntity.ok(accountService.passwordRecovery());
    }


}
