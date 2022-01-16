package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.service.StorageLoggingService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> findPosts(
            PostSearchRQ postSearchRQ,
            ElementPageable pageable) {
        return ResponseEntity.ok(postService.searchPosts(postSearchRQ, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePostById(
            @PathVariable int id,
            @RequestParam (defaultValue = "0")  long publish_date,
            @RequestBody PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.changePostById(id, publish_date, postChangeRQ)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.deletePostById(id)));
    }

    @PutMapping("/{id}/recover")
    public ResponseEntity<?> recoverPostById(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.recoverPostById(id)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByPostId(
            @PathVariable int id,
            ElementPageable pageable){
        return ResponseEntity.ok(postService.getCommentsToPost(id, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> makeCommentToThePost(
            @PathVariable int id,
            @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.makeCommentToPost(id, commentRQ)));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> rewriteCommentToPost(
            @PathVariable int id,
            @PathVariable int comment_id,
            @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.rewriteCommentToThePost(id, comment_id, commentRQ)));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> deleteCommentById(
            @PathVariable int id,
            @PathVariable int comment_id){
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.deleteCommentToThePost(id, comment_id)));
    }

    @PutMapping("/{id}/comments/{comment_id}/recover}")
    public ResponseEntity<?> recoverCommentToPost(
            @PathVariable int id,
            @PathVariable int comment_id){
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.recoverCommentToPost(id, comment_id)));
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<?> reportPostById(@PathVariable int id){
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.reportPostById(id)));
    }

    @PostMapping("/{id}/comments/{comment_id}/report")
    public ResponseEntity<?> reportCommentToPost(@PathVariable int id,
                                     @PathVariable int
                                             comment_id){
        return ResponseEntity.ok(postService.reportCommentToThePost(id, comment_id));
    }

}
