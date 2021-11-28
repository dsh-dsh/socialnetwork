package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUser() throws JsonProcessingException {
        int id = 1; // get from token
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/me")
    public ResponseEntity<?> editUser(@RequestBody UserChangeRQ userChangeRQ) {
        int id = 1;//take id from auth token
        return ResponseEntity.ok(userService.editUser(id, userChangeRQ));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser() {
        int id = 1;//take id from auth token
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<?> getUserWall(@PathVariable int id,
                                         @RequestParam(defaultValue = "0") int offset,
                                         @RequestParam(defaultValue = "20") int itemPerPage) {
        return ResponseEntity.ok(userService.getUserWall(id, offset, itemPerPage));
    }

    @PostMapping("/{id}/wall")
    public ResponseEntity<?> addPostToUserWall(@PathVariable int id,
                                               @RequestParam(name = "publish_date") long publishDate,
                                               @RequestBody PostChangeRQ postChangeRQ) {
        return ResponseEntity.ok(userService.addPostToUserWall(id, publishDate, postChangeRQ));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(  // TODO check required
            @RequestParam(name = "first_name", required = false) String firstName,
            @RequestParam(name = "last_name", required = false) String lastName,
            @RequestParam(name = "age_from", required = false, defaultValue = "0") int ageFrom,
            @RequestParam(name = "age_to", required = false, defaultValue = "0") int ageTo,
            @RequestParam(name = "city_id", required = false, defaultValue = "0") int cityId,
            @RequestParam int offset,
            @RequestParam int itemPerPage) {

        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(ageFrom);
        System.out.println(ageTo);
        System.out.println(cityId);

    return ResponseEntity.ok(userService.searchUsers(firstName, lastName, ageFrom, ageTo, cityId, offset, itemPerPage));
}

//    @GetMapping("/search")
//    public ResponseEntity<?> searchUsers(  // TODO check required
//                                       @ModelAttribute SearchRQ searchRQ,
//                                       @RequestParam int offset,
//                                       @RequestParam int itemPerPage) {
//    return ResponseEntity.ok(userService.searchUsers(searchRQ, offset, itemPerPage));
//}

    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable int id){
        return ResponseEntity.ok(userService.blockUser(id));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable int id){
        return ResponseEntity.ok(userService.unblockUser(id));
    }
}
