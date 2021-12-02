package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Semen V
 * @created 19|11|2021
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping("/friends")
    public ResponseEntity<?> getAllFriends(@RequestParam(defaultValue = "") String name,
                                           Pageable pageable) {
        return ResponseEntity.ok(friendsService.getAllFriends(name, pageable));
    }

    @DeleteMapping("/friends/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable int id) {
        return ResponseEntity.ok(friendsService.deleteFriend(id));
    }

    @PostMapping("/friends/{id}")
    public ResponseEntity<?> addFriend(@PathVariable int id) {
        return ResponseEntity.ok(friendsService.addFriend(id));
    }

    @GetMapping("/friends/request")
    public ResponseEntity<?> getRequests(
            @RequestParam(defaultValue = "") String name,
            Pageable pageable) {
        return ResponseEntity.ok(friendsService.getRequests(name, pageable));
    }

    @GetMapping("/friends/recommendations")
    public ResponseEntity<?> getRecommendations(Pageable pageable) {
        return ResponseEntity.ok(friendsService.getRecommendations(pageable));
    }

    @PostMapping("/is/friends")
    public ResponseEntity<?> isFriends(@RequestBody List<Integer> user_ids) {
        return ResponseEntity.ok(friendsService.isFriends(user_ids));
    }



}
