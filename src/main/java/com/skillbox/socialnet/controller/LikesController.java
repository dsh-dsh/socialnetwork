package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.DefaultRS;
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
    public ResponseEntity<DefaultRS> like() {
        DefaultRS defaultRS = likeService.like();
        return ResponseEntity.ok(defaultRS);
    }

    @DeleteMapping
    public ResponseEntity<DefaultRS> dislike() {
        DefaultRS defaultRS = likeService.dislike();
        return ResponseEntity.ok(defaultRS);
    }

}
