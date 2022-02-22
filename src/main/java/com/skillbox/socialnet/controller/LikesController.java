package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.DeleteLikeDTO;
import com.skillbox.socialnet.model.dto.LikeDTO;
import com.skillbox.socialnet.model.dto.LikedDTO;
import com.skillbox.socialnet.model.rq.LikeRQ;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.service.AuthService;
import com.skillbox.socialnet.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikesController {

    private final LikeService likeService;
    private final AuthService authService;

    @GetMapping("/likes")
    public ResponseEntity<GeneralResponse<LikeDTO>> getLikes(
            @RequestParam("item_id") int itemId,
            @RequestParam String type){
        return ResponseEntity.ok(new GeneralResponse<>(likeService.getLikes(itemId)));
    }

    @PutMapping("/likes")
    public ResponseEntity<GeneralResponse<LikeDTO>> setLike(
            @RequestBody LikeRQ likeRQ){
        return ResponseEntity.ok(new GeneralResponse<>(likeService.setLike(likeRQ.getId())));
    }

    @GetMapping("/liked")
    public ResponseEntity<GeneralResponse<LikedDTO>> getLiked(
            @RequestParam("item_id") int postId,
            @RequestParam String type,
            @RequestParam("user_id") int userId){
        return ResponseEntity.ok(new GeneralResponse<>(likeService.getLiked(postId, userId)));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<GeneralResponse<DeleteLikeDTO>> deleteLike(
            @RequestParam("item_id") int itemId,
            @RequestParam String type){
        return ResponseEntity.ok(new GeneralResponse<>(likeService.deleteLike(itemId)));
    }


}
