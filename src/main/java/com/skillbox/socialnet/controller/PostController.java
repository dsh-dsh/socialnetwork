package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.DeleteDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.rq.CommentRQ;
import com.skillbox.socialnet.model.rq.PostChangeRQ;
import com.skillbox.socialnet.model.rq.PostSearchRQ;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.service.AuthService;
import com.skillbox.socialnet.service.CommentService;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final AuthService authService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<GeneralListResponse<PostDTO>> searchPosts(
            PostSearchRQ postSearchRQ,
            ElementPageable pageable) {
        return ResponseEntity.ok(postService.searchPosts(postSearchRQ, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<PostDTO>> getPost(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<PostDTO>> editPost(
            @PathVariable int id,
            @RequestParam(defaultValue = "0", name = "publishDate") long publishDate,
            @RequestBody @Valid PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.changePostById(id, publishDate, postChangeRQ)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<DeleteDTO>> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.deletePostById(id)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<GeneralListResponse<CommentDTO>> getComments(@PathVariable int id){
        return ResponseEntity.ok(
                new GeneralListResponse<>(postService.getCommentsToPost(id)));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<GeneralResponse<CommentDTO>> postComment(
            @PathVariable(name = "id") int postId,
            @RequestBody @Valid CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(postService.makeCommentToPost(postId, commentRQ)));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<GeneralResponse<CommentDTO>> editComment(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId,
            @RequestBody @Valid CommentRQ commentRQ) {
        return ResponseEntity.ok(
                new GeneralResponse<>(commentService.rewriteCommentToThePost(commentId, commentRQ)));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<GeneralResponse<DeleteDTO>> deleteComment(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId) {
        return ResponseEntity.ok(
                new GeneralResponse<>(commentService.deleteCommentToThePost(id, commentId)));
    }

}
