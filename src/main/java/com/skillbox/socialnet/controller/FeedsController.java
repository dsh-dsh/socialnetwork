package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedsController {


    private final PostService postService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<PostDTO>> getFeeds(@RequestParam(defaultValue = "") String name,
            Pageable pageable){
        return ResponseEntity.ok(new GeneralListResponse<>(postService.getFeeds(), pageable));
    }



}
