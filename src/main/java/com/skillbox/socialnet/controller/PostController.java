package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.service.CommentService;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class    PostController {

    private final PostService postService;
    private final CommentService commentService;

    @MethodLog
    @GetMapping
    public ResponseEntity<?> searchPosts(
            PostSearchRQ postSearchRQ,
            ElementPageable pageable) {
        return ResponseEntity.ok(postService.searchPosts(postSearchRQ, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(
            @PathVariable int id,
            @RequestParam (defaultValue = "0")  long publish_date,
            @RequestBody @Valid PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.changePostById(id, publish_date, postChangeRQ)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.deletePostById(id)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable int id,
            ElementPageable pageable){
        return ResponseEntity.ok(postService.getCommentsToPost(id, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> postComment(
            @PathVariable(name = "id") int postId,
            @RequestBody @Valid CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.makeCommentToPost(postId, commentRQ)));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> editComment(
            @PathVariable int id,
            @PathVariable int comment_id,
            @RequestBody @Valid CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(commentService.rewriteCommentToThePost(id, comment_id, commentRQ)));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable int id,
            @PathVariable int comment_id){
        return ResponseEntity.ok(
                new GeneralResponse<>(commentService.deleteCommentToThePost(id, comment_id)));
    }

}
