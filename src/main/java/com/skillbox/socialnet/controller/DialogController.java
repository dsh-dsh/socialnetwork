package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.service.DialogService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.anotation.MethodLog;
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
    public ResponseEntity<GeneralListResponse<DialogDTO>>
    getDialogs(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ElementPageable pageable) {

        return ResponseEntity.ok(dialogService.getDialogs(pageable));
    }

    @PostMapping
    public ResponseEntity<GeneralResponse<DialogIdDTO>>
    createDialog(@RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
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

    @MethodLog
    @GetMapping("/{dialog_id}/messages")
    public ResponseEntity<GeneralListResponse<MessageDTO>>
    getMessages(
            @PathVariable(name = "dialog_id") long dialogId,
            @RequestParam(required = false) ElementPageable pageable) {

        return ResponseEntity.ok(dialogService.getMessagesInDialog(dialogId, pageable));
    }

    @MethodLog
    @GetMapping("/unreaded")
    public ResponseEntity<GeneralResponse<UnreadCountDTO>>
    unread() {
        GeneralResponse<UnreadCountDTO> response =
                new GeneralResponse<>(dialogService.getUnreadCount());

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @PutMapping("/{dialog_id}/messages/{message_id}/read")
    public ResponseEntity<GeneralResponse<MessageResponseDTO>>
    readMessage(
            @PathVariable(name = "dialog_id") long dialogId,
            @PathVariable(name = "message_id") int messageId) {
        GeneralResponse<MessageResponseDTO> response =
                new GeneralResponse<>(dialogService.readMessage(dialogId, messageId));

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @DeleteMapping("{dialog_id}")
    public ResponseEntity<GeneralResponse<DialogIdDTO>>
    deleteDialog(@PathVariable(name = "dialog_id") long dialogId) {
        GeneralResponse<DialogIdDTO> response =
            new GeneralResponse<>(dialogService.deleteDialog(dialogId));

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @PutMapping("/{id}/users")
    public ResponseEntity<GeneralResponse<DialogCreateDTORequest>>
    addUsersToDialog(
            @PathVariable long id,
            @RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        GeneralResponse<DialogCreateDTORequest> response =
                new GeneralResponse<>(dialogService.addUsersToDialog(id, dialogCreateDTORequest));

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @DeleteMapping("/{id}/users/{ids}")
    public ResponseEntity<GeneralResponse<DialogCreateDTORequest>>
    deleteUsersFromDialog(
            @PathVariable long id,
            @PathVariable String ids) {
        GeneralResponse<DialogCreateDTORequest> response =
                new GeneralResponse<>(dialogService.deleteUsersFromDialog(id, ids));

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @GetMapping("/{id}/users/invite")
    public ResponseEntity<GeneralResponse<InviteLinkDTO>>
    getLinkToJoinDialog(@PathVariable long id) {
        GeneralResponse<InviteLinkDTO> response =
                new GeneralResponse<>(dialogService.getLinkToJoin(id));

        return ResponseEntity.ok(response);
    }

    //покаНеИспользуется
    @GetMapping("/{id}/users/join")
    public ResponseEntity<GeneralResponse<DialogCreateDTORequest>>
    joinDialogByLink(@PathVariable long id) {
        GeneralResponse<DialogCreateDTORequest> response =
                new GeneralResponse<>(dialogService.joinByLink(id));

        return ResponseEntity.ok(response);
    }

}
