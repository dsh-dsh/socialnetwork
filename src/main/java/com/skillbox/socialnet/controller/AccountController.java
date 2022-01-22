package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.NotificationSettingsDto;
import com.skillbox.socialnet.service.AccountService;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> register(
            @RequestBody @Valid AccountRegisterRQ accountRegisterRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.register(accountRegisterRQ));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/password/recovery")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> passwordRecovery(
            @RequestBody AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.recoveryPassword(accountEmailRQ, servletRequest));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/password/set")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setPassword(
            @RequestBody AccountPasswordSetRQ accountPasswordSetRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setPassword(accountPasswordSetRQ));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/shift-email")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> shiftEmail(HttpServletRequest servletRequest) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.shiftEmail(servletRequest));

        return ResponseEntity.ok(response);
    }

    @MethodLog
    @PutMapping("/email")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setEmail(
            @RequestBody @Valid AccountEmailRQ accountEmailRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setEmail(accountEmailRQ));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/notifications")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setNotifications(
            @RequestBody AccountNotificationRQ accountNotificationRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setNotifications(accountNotificationRQ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications")
    public ResponseEntity<GeneralListResponse<NotificationSettingsDto>> getNotifications(Pageable pageable) {
        GeneralListResponse<NotificationSettingsDto> response =
                new GeneralListResponse<>(accountService.getNotifications(), pageable);

        return ResponseEntity.ok(response);
    }


}
