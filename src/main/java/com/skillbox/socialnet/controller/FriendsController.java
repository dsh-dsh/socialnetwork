package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.service.FriendsService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<UserDTO>> getAllFriends(ElementPageable pageable) {
        return ResponseEntity.ok(friendsService.getAllFriends(pageable));
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
    public ResponseEntity<GeneralListResponse<UserDTO>> getRequests(ElementPageable pageable) {
        return ResponseEntity.ok(friendsService.getRequests(pageable));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<GeneralListResponse<UserDTO>> getRecommendations() {
        return ResponseEntity.ok(
                new GeneralListResponse<>(friendsService.getRecommendations()));
    }



}
