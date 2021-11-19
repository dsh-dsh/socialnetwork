package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Semen V
 * @created 19|11|2021
 */

@RestController
@RequestMapping("/api/v1/")
public class FriendsController {

    @Autowired
    private FriendsService friendsService;

    @GetMapping("/friends")
    public ResponseEntity<?> getAllFriends(@RequestParam String name,
                                           @RequestParam int offset,
                                           @RequestParam int itemPerPage) {
        return ResponseEntity.ok(friendsService.getAllFriends(name, offset, itemPerPage));
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
    public ResponseEntity<?> getRequests(@RequestParam String name,
                                         @RequestParam Integer offset,
                                         @RequestParam Integer itemPerPage) {
        return ResponseEntity.ok(friendsService.getRequests(name,offset, itemPerPage));
    }

    @GetMapping("/friends/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam Integer offset,
                                                @RequestParam Integer itemPerPage) {
        return ResponseEntity.ok(friendsService.getRecommendations(offset,itemPerPage));
    }

    @PostMapping("/is/friends")
    public ResponseEntity<?> isFriends(@RequestBody List<Integer> user_ids) {
        return ResponseEntity.ok(friendsService.isFriends(user_ids));
    }



}
