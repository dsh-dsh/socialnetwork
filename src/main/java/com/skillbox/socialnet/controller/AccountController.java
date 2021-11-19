package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Semen V
 * @created 18|11|2021
 */

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //!
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountRegisterRQ accountRegisterRQ) {
        return ResponseEntity.ok(accountService.register());
    }
    //!
    @PutMapping("/password/recovery")
    public ResponseEntity<?> passwordRecovery(@RequestBody AccountEmailRQ acctEmailRequest) {
        return ResponseEntity.ok(accountService.recoveryPassword());
    }

    @PutMapping("/password/set")
    public ResponseEntity<?> setPassword(@RequestBody AccountPasswordSetRQ accountPasswordSetRQ) {
        return ResponseEntity.ok(accountService.setPassword(accountPasswordSetRQ));
    }

    @PutMapping("/email")
    public ResponseEntity<?> setEmail(@RequestBody AccountEmailRQ acctEmailRequest) {
        return ResponseEntity.ok(accountService.setEmail(acctEmailRequest));
    }

    //!
    @PutMapping("/notifications")
    public ResponseEntity<?> setNotifications(@RequestBody AccountNotificationRQ accountNotificationRQ) {
        return ResponseEntity.ok(accountService.setNotifications(accountNotificationRQ));
    }


}
