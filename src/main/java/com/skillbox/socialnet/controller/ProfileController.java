package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserSearchRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.service.PostService;
import com.skillbox.socialnet.service.UserService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse<UserDTO>> getUser() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.getUser()));
    }

    @MethodLog
    @PutMapping("/me")
    public ResponseEntity<GeneralResponse<UserDTO>> editUser(
            @RequestBody @Valid UserChangeRQ userChangeRQ,
            HttpServletRequest request) {
        return ResponseEntity.ok(new GeneralResponse<>(userService.editUser(userChangeRQ)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<GeneralResponse<String>> deleteUser() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.deleteUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<UserDTO>> getUser(@PathVariable int id) {
        return ResponseEntity.ok(new GeneralResponse<>(userService.getUserById(id)));
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<GeneralListResponse<PostDTO>> getUserWall(
            @PathVariable int id, ElementPageable pageable) {
        return ResponseEntity.ok(new GeneralListResponse<>(postService.getUserWall(id, pageable), pageable));
    }

    @PostMapping("/{id}/wall")
    public ResponseEntity<GeneralResponse<PostDTO>> addPostToUserWall(
            @PathVariable int id,
            @RequestParam(name = "publish_date", defaultValue = "0") long publishDate,
            @RequestBody PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(new GeneralResponse<>(postService.addPostToUserWall(id, publishDate, postChangeRQ)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(name = "first_or_last_name", required = false) String firstOrLastName,
            @RequestParam(name = "first_name", required = false) String firstName,
            @RequestParam(name = "last_name", required = false) String lastName,
            @RequestParam(name = "age_from", defaultValue = "0") int ageFrom,
            @RequestParam(name = "age_to", defaultValue = "0") int ageTo,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            Pageable pageable) {

        GeneralListResponse<?> response;
        if(firstOrLastName != null) {
            response = userService.searchUsers(firstOrLastName, pageable);
        } else {
            UserSearchRQ userSearchRQ = new UserSearchRQ(firstName, lastName, ageFrom, ageTo, country, city);
            response = userService.searchUsers(userSearchRQ, pageable);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> blockUser(@PathVariable int id) {
        return ResponseEntity.ok(new GeneralResponse<>(userService.blockUser(id)));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> unblockUser(@PathVariable int id) {
        return ResponseEntity.ok(new GeneralResponse<>(userService.unblockUser(id)));
    }

    @PutMapping("/checkonline")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> checkOnline() {
        return ResponseEntity.ok(new GeneralResponse<>(userService.checkOnline()));
    }
}
