package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private FriendsService friendsService;

    //!
    @GetMapping("/friends")
    public ResponseEntity<?> getAllFriends(@RequestParam(defaultValue = "") String name,
                                           @RequestParam(defaultValue = "0") int offset,
                                           @RequestParam(defaultValue = "20") int itemPerPage) {
        return ResponseEntity.ok(friendsService.getAllFriends(name, offset, itemPerPage));
    }

    //!
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable int id) {
        return ResponseEntity.ok(friendsService.deleteFriend(id));
    }

    @PostMapping("/friends/{id}")
    public ResponseEntity<?> addFriend(@PathVariable int id) {
        return ResponseEntity.ok(friendsService.addFriend(id));
    }

    @GetMapping("/friends/request")
    public ResponseEntity<?> getRequests(@RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "0") int offset,
                                         @RequestParam(defaultValue = "20") int itemPerPage) {
        return ResponseEntity.ok(friendsService.getRequests(name,offset, itemPerPage));
    }

    //!
    @GetMapping("/friends/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam(defaultValue = "0") int offset,
                                                @RequestParam(defaultValue = "20") int itemPerPage) {
        return ResponseEntity.ok(friendsService.getRecommendations(offset,itemPerPage));
    }

    @PostMapping("/is/friends")
    public ResponseEntity<?> isFriends(@RequestBody List<Integer> user_ids) {
        return ResponseEntity.ok(friendsService.isFriends(user_ids));
    }



}
