package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<DefaultRS> getPosts(PostSearchRQ postSearchRQ, Pageable pageable) {

        return ResponseEntity.ok(postService.searchPosts(postSearchRQ, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultRS> getPostById(
            @PathVariable int id) {

        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultRS> changePostById(
            @PathVariable int id,
            @RequestBody PostChangeRQ postChangeRQ) {

        return ResponseEntity.ok(postService.changePostById(id, postChangeRQ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultRS> deletePostById(
            @PathVariable int id) {

        return ResponseEntity.ok(postService.deletePostById(id));
    }

    @PutMapping("/{id}/recover")
    public ResponseEntity<DefaultRS> recoverPostById(
            @PathVariable int id) {

        return ResponseEntity.ok(postService.recoverPostById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<DefaultRS> getCommentsByPostId(
            @PathVariable int id,
            Pageable pageable) {

        return ResponseEntity.ok(postService.getCommentsToPost(id, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<DefaultRS> makeCommentToThePost(
            @PathVariable int id,
            @RequestBody CommentRQ commentRQ) {

        return ResponseEntity.ok(postService.makeCommentToPost(id, commentRQ));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<DefaultRS> rewriteCommentToPost(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId,
            @RequestBody CommentRQ commentRQ) {

        return ResponseEntity.ok(postService.rewriteCommentToThePost(id, commentId, commentRQ));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<DefaultRS> deleteCommentById(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId) {

        return ResponseEntity.ok(postService.deleteCommentToThePost(id, commentId));
    }

    @PutMapping("/{id}/comments/{comment_id}/recover}")
    public ResponseEntity<DefaultRS> recoverCommentToPost(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId) {

        return ResponseEntity.ok(postService.recoverCommentToPost(id, commentId));
    }


    @PostMapping("/{id}/report")
    public ResponseEntity<DefaultRS> reportPostById(
            @PathVariable int id) {

        return ResponseEntity.ok(postService.reportPostById(id));
    }

    @PostMapping("/{id}/comments/{comment_id}/report")
    public ResponseEntity<DefaultRS> reportCommentToPost(
            @PathVariable int id,
            @PathVariable(name = "comment_id") int commentId) {

        return ResponseEntity.ok(postService.reportCommentToThePost(id, commentId));
    }

}
