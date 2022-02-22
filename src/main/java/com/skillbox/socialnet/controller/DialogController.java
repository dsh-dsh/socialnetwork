package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rq.DialogCreateDTORequest;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.service.DialogService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
public class DialogController {

    private final DialogService dialogService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<DialogDTO>> getDialogs() {
        return ResponseEntity.ok(
                new GeneralListResponse<>(dialogService.getDialogs()));
    }

    @PostMapping
    public ResponseEntity<GeneralResponse<DialogIdDTO>> createDialog(
            @RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        GeneralResponse<DialogIdDTO> response =
            new GeneralResponse<>(dialogService.createDialog(dialogCreateDTORequest));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{dialog_id}/messages")
    public ResponseEntity<GeneralResponse<MessageDTO>> sendMessage(
            @PathVariable(name = "dialog_id") long dialogId,
            @RequestBody MessageSendDtoRequest messageSendDtoRequest) {
        GeneralResponse<MessageDTO> response =
                new GeneralResponse<>(dialogService.sendMessage(dialogId, messageSendDtoRequest));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{dialog_id}/messages")
    public ResponseEntity<GeneralListResponse<MessageDTO>> getMessages(
            @PathVariable(name = "dialog_id") long dialogId,  ElementPageable pageable) {

        return ResponseEntity.ok(dialogService.getMessagesInDialog(dialogId, pageable));
    }

    @GetMapping("/unreaded")
    public ResponseEntity<GeneralResponse<UnreadCountDTO>> unread() {
        GeneralResponse<UnreadCountDTO> response =
                new GeneralResponse<>(dialogService.getUnreadCount());

        return ResponseEntity.ok(response);
    }


}
