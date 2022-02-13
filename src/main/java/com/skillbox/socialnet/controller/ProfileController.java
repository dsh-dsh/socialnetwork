package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserSearchRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.FriendsService;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.service.UserService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;
    private final FriendsService friendsService;

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse<UserDTO>> getUser() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.getUser()));
    }

    @PutMapping("/me")
    public ResponseEntity<GeneralResponse<UserDTO>> editUser(
            @RequestBody @Valid UserChangeRQ userChangeRQ) {
        return ResponseEntity.ok(new GeneralResponse<>(userService.editUser(userChangeRQ)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<GeneralResponse<String>> deleteUser() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.deleteUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<UserDTO>> getUser(
            @PathVariable int id) {
        return ResponseEntity.ok(
                new GeneralResponse<>(userService.getUserById(id)));
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<GeneralListResponse<PostDTO>> getUserWall(
            @PathVariable int id, ElementPageable pageable) {
        return ResponseEntity.ok(postService.getUserWall(id, pageable));
    }

    @PostMapping("/{id}/wall")
    public ResponseEntity<GeneralResponse<PostDTO>> addPostToUserWall(
            @PathVariable(name = "id") int personId,
            @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
            @RequestBody @Valid PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(new GeneralResponse<>(postService.addPostToUserWall(personId, publishDate, postChangeRQ)));
    }

    @Loggable
    @GetMapping("/search")
    public ResponseEntity<GeneralListResponse<UserDTO>> searchUsers(
            @RequestParam(name = "first_or_last_name", required = false) String firstOrLastName,
            @RequestParam(name = "first_name", required = false) String firstName,
            @RequestParam(name = "last_name", required = false) String lastName,
            @RequestParam(name = "age_from", defaultValue = "0") int ageFrom,
            @RequestParam(name = "age_to", defaultValue = "0") int ageTo,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            ElementPageable pageable) {

        GeneralListResponse<UserDTO> response;
        if(firstOrLastName != null) {
            response = userService.searchUsers(firstOrLastName, pageable);
        } else {
            UserSearchRQ userSearchRQ = new UserSearchRQ(firstName, lastName, ageFrom, ageTo, country, city);
            response = userService.searchUsers(userSearchRQ, pageable);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/block/{personId}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> blockUser(
            @PathVariable int personId) {
        return ResponseEntity.ok(
                new GeneralResponse<>(friendsService.blockUser(personId)));
    }

    @DeleteMapping("/block/{personId}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> unblockUser(
            @PathVariable int personId) {
        return ResponseEntity.ok(
                new GeneralResponse<>(friendsService.unblockUser(personId)));
    }

    @PutMapping("/checkonline")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> checkOnline() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.checkOnline()));
    }
}
