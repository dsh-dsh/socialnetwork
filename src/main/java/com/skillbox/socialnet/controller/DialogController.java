package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.dto.MessageResponseDTO;
import com.skillbox.socialnet.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
            @RequestParam(required = false) Pageable pageable) { // TODO а так вообще можно?
        return ResponseEntity.ok(dialogService.getDialogs(query, pageable));
    }

    @PostMapping
    public ResponseEntity<?> createDialog(@RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        dialogCreateDTORequest.getUserIds().forEach(System.out::println);
        return ResponseEntity.ok(dialogService.createDialog(dialogCreateDTORequest));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable long id,
            @RequestBody MessageResponseDTO messageResponseDTO) {
        return ResponseEntity.ok(dialogService.sendMessage(id, messageResponseDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteDialog(@PathVariable long id) {
        return ResponseEntity.ok(dialogService.deleteDialog(id));
    }

    @PutMapping("/{id}/users")
    public ResponseEntity<?> addUsersToDialog(
            @PathVariable long id,
            @RequestBody DialogCreateDTORequest dialogCreateDTORequest) {
        return ResponseEntity.ok(dialogService.addUsersToDialog(id, dialogCreateDTORequest));
    }

    @DeleteMapping("/{id}/users/{ids}")
    public ResponseEntity<?> deleteUsersFromDialog(
            @PathVariable long id,
            @PathVariable String ids) {
        return ResponseEntity.ok(dialogService.deleteUsersFromDialog(id, ids));
    }

    @GetMapping("/{id}/users/invite")
    public ResponseEntity<?> getLinkToJoinDialog(@PathVariable long id) {
        return ResponseEntity.ok(dialogService.getLinkToJoin(id));
    }

    @GetMapping("/{id}/users/join")
    public ResponseEntity<?> joinDialogByLink(@PathVariable long id) {
        return ResponseEntity.ok(dialogService.joinByLink(id));
    }

    @GetMapping("/unreaded")
    public ResponseEntity<?> unread() {
        return ResponseEntity.ok(dialogService.unread());
    }

}
