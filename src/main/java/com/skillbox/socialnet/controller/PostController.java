package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.CommentRequest;
import com.skillbox.socialnet.model.RQ.PostChangeRequest;
import com.skillbox.socialnet.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {


    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam(defaultValue = "") String text,
            @RequestParam long date_from,
            @RequestParam long date_to,
            @RequestParam int offset,
            @RequestParam(defaultValue = "20") int itemPerPage
    ) {
        postService.getPostsByText(text, date_from, date_to, offset, itemPerPage);
        return ResponseEntity.ok(postService.getPostsByText(text, date_from, date_to, offset, itemPerPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePostById(@PathVariable long id,
                               @PathVariable long publish_date,
                               @RequestBody PostChangeRequest postChangeRequest
                               ) {
        return ResponseEntity.ok(postService.changePostById(id, publish_date, postChangeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable long id) {

        return ResponseEntity.ok(postService.deletePostById(id));

    }

    @PutMapping("/{id}/recover")
    public ResponseEntity<?> recoverPostById(@PathVariable long id) {
        return ResponseEntity.ok(postService.recoverPostById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable long id,
                                    @RequestParam int offset,
                                    @RequestParam(defaultValue = "20") int itemPerPage

    ){

        return ResponseEntity.ok(postService.getCommentsToPost(id, offset, itemPerPage));

    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> makeCommentToThePost(@PathVariable long id,
                                     @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(postService.makeCommentToPost(id, commentRequest));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> rewriteCommentToPost(@PathVariable long id,
                                     @PathVariable long comment_id,
                                     @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(postService.rewriteCommentToThePost(id, comment_id, commentRequest));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable long id,
                                  @PathVariable long comment_id){

        return ResponseEntity.ok(postService.deleteCommentToThePost(id, comment_id));

    }

    @PutMapping("/{id}/comments/{comment_id}/recover}")
    public ResponseEntity<?> recoverCommentToPost(@PathVariable long id,
                                     @PathVariable long comment_id){

        return ResponseEntity.ok(postService.recoverCommentToPost(id, comment_id));

    }


    @PostMapping("/{id}/report")
    public ResponseEntity<?> reportPostById(@PathVariable String id){

        return ResponseEntity.ok(postService.reportPostById(id));

    }

    @PostMapping("/{id}/comments/{comment_id}/report")
    public ResponseEntity<?> reportCommentToPost(@PathVariable long id,
                                     @PathVariable long comment_id){
        return ResponseEntity.ok(postService.reportCommentToThePost(id, comment_id));

    }

}
