package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.SearchRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/me")
    public ResponseEntity<?> getUser(HttpServletRequest request) throws JsonProcessingException {
        if (jwtProvider.getTokenFromRequest(request) != null) {
            return ResponseEntity.ok(userService.getUser(jwtProvider.getUserNameFromToken(jwtProvider.getTokenFromRequest(request)),
                    jwtProvider.getTokenFromRequest(request)));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/me")
    public ResponseEntity<?> editUser(@RequestBody UserChangeRQ userChangeRQ,
                                      HttpServletRequest request) {
        if (jwtProvider.getTokenFromRequest(request) != null) {
            return ResponseEntity.ok(userService.editUser(jwtProvider.getUserNameFromToken(jwtProvider.getTokenFromRequest(request)),
                    userChangeRQ, jwtProvider.getTokenFromRequest(request)));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser() {
        int id = 1;//take id from auth token
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<?> getUserWall(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage) {
        return ResponseEntity.ok(userService.getUserWall(id, offset, itemPerPage));
    }

    @PostMapping("/{id}/wall")
    public ResponseEntity<?> addPostToUserWall(
            @PathVariable int id,
            @RequestParam(name = "publish_date") long publishDate,
            @RequestBody PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(userService.addPostToUserWall(id, publishDate, postChangeRQ));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @ModelAttribute SearchRQ searchRQ,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage) {
        System.out.println(searchRQ);
        return ResponseEntity.ok(userService.searchUsers(searchRQ, offset, itemPerPage));
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.blockUser(id));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.unblockUser(id));
    }
}
