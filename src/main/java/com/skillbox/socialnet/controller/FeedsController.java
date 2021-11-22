package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedsController {


    private final PostService postService;

    public FeedsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> getFeeds(
            @RequestParam(defaultValue = "") String name,
            @RequestParam (defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage
    ){
        return ResponseEntity.ok(postService.getFeeds(name, offset, itemPerPage));
    }



}
