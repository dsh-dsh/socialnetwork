package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.Constants;
import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class MyController {

    private final UserService userService;
    private final PersonModelMapper personModelMapper;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserRQ authUserRQ) {
        UserDTO userDTO = loginJwt(authUserRQ);
        return ResponseEntity.ok(userDTO);
    }

    private UserDTO loginJwt(AuthUserRQ authUserRQ) {
        try{
            String email = authUserRQ.getEmail();
            String password = authUserRQ.getPassword();
            Person person = userService.getPersonByEmailAndPassword(email, password);
            if(person == null) {
                throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
            }
            UserDTO userDTO = personModelMapper.mapToUserDTO(person);
            String token = jwtProvider.generateToken(email);
            userDTO.setToken(token);
            return userDTO;

        } catch (AuthenticationException authenticationException) {
            authenticationException.printStackTrace();
        }
        return null;
    }

    @GetMapping("/token/access")
    public String tokenAccess() {
        return "access";
    }

}
