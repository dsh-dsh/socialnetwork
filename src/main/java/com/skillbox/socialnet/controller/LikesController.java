package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.LikeDTO;
import com.skillbox.socialnet.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikesController {

    private final LikeService likeService;

    @GetMapping("/likes")
    public ResponseEntity<GeneralResponse<LikeDTO>> getLikes(@RequestParam("item_id") int itemId,
                                                             @RequestParam String type){
        return ResponseEntity.ok(new GeneralResponse<>(likeService.getLikes(itemId)));
    }



}
