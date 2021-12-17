package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
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
    public ResponseEntity<?> getPosts(
            @RequestParam(defaultValue = "") String text,
            @RequestParam (name = "date_from", defaultValue = "0") long dateFrom,
            @RequestParam (name = "date_to", defaultValue = "0") long dateTo,
            Pageable pageable) {
        return ResponseEntity.ok(postService.findPostsByTextOrTitle(text, dateFrom, dateTo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePostById(@PathVariable int id,
                               @RequestParam (defaultValue = "0")  long publish_date,
                               @RequestBody PostChangeRQ postChangeRQ
                               ) {
        return ResponseEntity.ok(postService.changePostById(id, publish_date, postChangeRQ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable int id) {

        return ResponseEntity.ok(postService.deletePostById(id));

    }

    @PutMapping("/{id}/recover")
    public ResponseEntity<?> recoverPostById(@PathVariable int id) {
        return ResponseEntity.ok(postService.recoverPostById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable int id, Pageable pageable){

        return ResponseEntity.ok(postService.getCommentsToPost(id, pageable));

    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> makeCommentToThePost(@PathVariable int id,
                                     @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(postService.makeCommentToPost(id, commentRQ));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> rewriteCommentToPost(
            @PathVariable int id,
            @PathVariable int comment_id,
            @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(postService.rewriteCommentToThePost(id, comment_id, commentRQ));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> deleteCommentById(
            @PathVariable int id,
            @PathVariable int comment_id){
        return ResponseEntity.ok(postService.deleteCommentToThePost(id, comment_id));

    }

    @PutMapping("/{id}/comments/{comment_id}/recover}")
    public ResponseEntity<?> recoverCommentToPost(
            @PathVariable int id,
            @PathVariable int comment_id){
        return ResponseEntity.ok(postService.recoverCommentToPost(id, comment_id));

    }


    @PostMapping("/{id}/report")
    public ResponseEntity<?> reportPostById(@PathVariable int id){

        return ResponseEntity.ok(postService.reportPostById(id));

    }

    @PostMapping("/{id}/comments/{comment_id}/report")
    public ResponseEntity<?> reportCommentToPost(@PathVariable int id,
                                     @PathVariable int
                                             comment_id){
        return ResponseEntity.ok(postService.reportCommentToThePost(id, comment_id));

    }

}
