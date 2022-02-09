package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.service.AuthService;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
@Loggable
public class FeedsController {

    private final PostService postService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<PostDTO>> getFeeds(
            @RequestParam(defaultValue = "") String name, ElementPageable pageable){
        if (authService.getPersonFromSecurityContext() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(postService.getFeeds(pageable));
    }

}
