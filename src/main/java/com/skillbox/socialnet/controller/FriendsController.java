package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping("/friends")
    public ResponseEntity<GeneralListResponse<UserDTO>> getAllFriends(
            @RequestParam(defaultValue = "") String name, Pageable pageable) {
        return ResponseEntity.ok(friendsService.getAllFriends(name, pageable));
    }

    @DeleteMapping("/friends/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> deleteFriend(@PathVariable int id) {
        return ResponseEntity.ok(new GeneralResponse<>(friendsService.deleteFriend(id)));
    }

    @PostMapping("/friends/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> addFriend(@PathVariable int id) {
        return ResponseEntity.ok(new GeneralResponse<>(friendsService.addFriend(id)));
    }

    @GetMapping("/friends/request")
    public ResponseEntity<?> getRequests(
            @RequestParam(defaultValue = "") String name, Pageable pageable) {
        return ResponseEntity.ok(friendsService.getRequests(name, pageable));
    }

    @GetMapping("/friends/recommendations")
    public ResponseEntity<?> getRecommendations(Pageable pageable) {
        return ResponseEntity.ok(friendsService.getRecommendations(pageable));
    }



}
