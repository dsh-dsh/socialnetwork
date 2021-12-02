package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikesController {

    private final LikeService likeService;

    @PutMapping
    public ResponseEntity<?> like() {
        return ResponseEntity.ok(likeService.like());
    }

    @DeleteMapping
    public ResponseEntity<?> dislike() {
        return ResponseEntity.ok(likeService.dislike());
    }

}
