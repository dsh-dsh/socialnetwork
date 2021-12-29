package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.service.DialogService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.anotation.ПокаНеИспользуется;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
public class DialogController {

    private final DialogService dialogService;

    @GetMapping
    public ResponseEntity<?> getDialogs(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ElementPageable pageable) {

        return ResponseEntity.ok(dialogService.getDialogs(pageable));
    }

    @PostMapping
    public ResponseEntity<?> createDialog(@RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        GeneralResponse<DialogIdDTO> response =
            new GeneralResponse<>(dialogService.createDialog(dialogCreateDTORequest));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{dialog_id}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable(name = "dialog_id") long dialogId,
            @RequestBody MessageSendDtoRequest messageSendDtoRequest) {
        GeneralResponse<MessageDTO> response =
                new GeneralResponse<>(dialogService.sendMessage(dialogId, messageSendDtoRequest));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{dialog_id}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable(name = "dialog_id") long dialogId,
            @RequestParam(required = false) ElementPageable pageable) {
        return ResponseEntity.ok(dialogService.getMessagesInDialog(dialogId, pageable));
    }

    @GetMapping("/unreaded")
    public ResponseEntity<?> unread() {
        GeneralResponse<UnreadCountDTO> response =
                new GeneralResponse<>(dialogService.getUnreadCount());
        return ResponseEntity.ok(response);
    }

    @ПокаНеИспользуется
    @PutMapping("/{dialog_id}/messages/{message_id}/read")
    public ResponseEntity<?> readMessage(
            @PathVariable(name = "dialog_id") long dialogId,
            @PathVariable(name = "message_id") int messageId) {
        GeneralResponse<MessageResponseDTO> response =
                new GeneralResponse<>(dialogService.readMessage(dialogId, messageId));
        return ResponseEntity.ok(response);
    }

    @ПокаНеИспользуется
    @DeleteMapping("{dialog_id}")
    public ResponseEntity<?> deleteDialog(@PathVariable(name = "dialog_id") long dialogId) {
        return ResponseEntity.ok(dialogService.deleteDialog(dialogId));
    }

    @ПокаНеИспользуется
    @PutMapping("/{id}/users")
    public ResponseEntity<?> addUsersToDialog(
            @PathVariable long id,
            @RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        return ResponseEntity.ok(dialogService.addUsersToDialog(id, dialogCreateDTORequest));
    }

    @ПокаНеИспользуется
    @DeleteMapping("/{id}/users/{ids}")
    public ResponseEntity<?> deleteUsersFromDialog(
            @PathVariable long id,
            @PathVariable String ids) {
        return ResponseEntity.ok(dialogService.deleteUsersFromDialog(id, ids));
    }

    @ПокаНеИспользуется
    @GetMapping("/{id}/users/invite")
    public ResponseEntity<?> getLinkToJoinDialog(@PathVariable long id) {
        return ResponseEntity.ok(dialogService.getLinkToJoin(id));
    }

    @ПокаНеИспользуется
    @GetMapping("/{id}/users/join")
    public ResponseEntity<?> joinDialogByLink(@PathVariable long id) {
        return ResponseEntity.ok(dialogService.joinByLink(id));
    }

}
