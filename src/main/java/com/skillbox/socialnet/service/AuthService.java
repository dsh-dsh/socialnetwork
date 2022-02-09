package com.skillbox.socialnet.service;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.model.rq.AuthUserRQ;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.security.CustomUserDetails;
import com.skillbox.socialnet.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final PersonService personService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public UserDTO login(AuthUserRQ authUserRQ) {
        Person person = personService.getPersonByEmail(authUserRQ.getEmail());
        if(!passwordEncoder.matches(authUserRQ.getPassword(), person.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(Constants.WRONG_CREDENTIALS_MESSAGE);
        }
        UserDTO userDTO = UserDTO.getUserDTO(person);
        userDTO.setToken(jwtProvider.generateToken(person));

        return userDTO;
    }


    public MessageOkDTO logout() {
        return new MessageOkDTO();
    }

    public Person getPersonFromSecurityContext() {
        try{
            CustomUserDetails securityUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return personRepository.findByeMail((securityUser.getUsername())).orElseThrow();
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException(Constants.NO_SUCH_USER_MESSAGE);
        }
    }


}
