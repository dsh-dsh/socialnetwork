package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedsController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<PostDTO>> getFeeds(ElementPageable pageable){
        return ResponseEntity.ok(postService.getFeeds(pageable));
    }

}
