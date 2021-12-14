package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedsController {


    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getFeeds(
            @RequestParam(defaultValue = "") String name,
            Pageable pageable){
        return ResponseEntity.ok(postService.getFeeds(name, pageable));
    }



}
