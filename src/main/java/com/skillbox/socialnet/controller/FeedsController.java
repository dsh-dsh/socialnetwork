package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.AuthService;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedsController {

    private final PostService postService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> getFeeds(
            @RequestParam(defaultValue = "") String name, ElementPageable pageable){
        if (authService.getPersonFromSecurityContext() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(postService.getFeeds(pageable));
    }

}
