package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
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
            @RequestParam (defaultValue = "0") long date_from,
            @RequestParam (defaultValue = "0") long date_to,
            @RequestParam (defaultValue = "0") int offset,
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
    public ResponseEntity<?> getCommentsByPostId(@PathVariable int id,
                                    @RequestParam (defaultValue = "0")  int offset,
                                    @RequestParam(defaultValue = "20") int itemPerPage

    ){

        return ResponseEntity.ok(postService.getCommentsToPost(id, offset, itemPerPage));

    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> makeCommentToThePost(@PathVariable int id,
                                     @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(postService.makeCommentToPost(id, commentRQ));
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> rewriteCommentToPost(@PathVariable int id,
                                     @PathVariable int comment_id,
                                     @RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(postService.rewriteCommentToThePost(id, comment_id, commentRQ));
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable int id,
                                  @PathVariable int
                                          comment_id){

        return ResponseEntity.ok(postService.deleteCommentToThePost(id, comment_id));

    }

    @PutMapping("/{id}/comments/{comment_id}/recover}")
    public ResponseEntity<?> recoverCommentToPost(@PathVariable int id,
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
