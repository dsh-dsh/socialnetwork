package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.FriendsService;
import com.skillbox.socialnet.util.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @Loggable
    @GetMapping
    public ResponseEntity<GeneralListResponse<UserDTO>> getAllFriends(
            @RequestParam(defaultValue = "") String name) {
        return ResponseEntity.ok(
                new GeneralListResponse<>(friendsService.getAllFriends(name)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> deleteFriend(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(friendsService.deleteFriend(id)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> addFriend(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(friendsService.addFriend(id)));
    }

    @GetMapping("/request")
    public ResponseEntity<GeneralListResponse<UserDTO>> getRequests(
            @RequestParam(defaultValue = "") String name) {
        return ResponseEntity.ok(
                new GeneralListResponse<>(friendsService.getRequests(name)));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<GeneralListResponse<UserDTO>> getRecommendations() {
        return ResponseEntity.ok(
                new GeneralListResponse<>(friendsService.getRecommendations()));
    }



}
