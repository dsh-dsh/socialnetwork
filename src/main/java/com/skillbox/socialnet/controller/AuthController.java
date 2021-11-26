package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Log
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserRQ authUserRQ) {
        DefaultRS<UserDTO> defaultRS = authService.loginJwt(authUserRQ);
        return ResponseEntity.ok(defaultRS);
    }

    @GetMapping("/admin/access")
    public String adminAccess() {
        return "admin access";
    }

    @GetMapping("/user/access")
    public String userAccess() {
        return "user access";
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login
//            (@RequestBody AuthUserRQ authUserRQ) {
//        DefaultRS defaultRS = authService.login(authUserRQ);
//        if (defaultRS.getError().equals("string")){
//            return ResponseEntity.ok(defaultRS);
//        }
//        return new ResponseEntity<>(defaultRS, HttpStatus.BAD_REQUEST);
//    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout () {
        return ResponseEntity.ok(authService.logout());
    }

}
