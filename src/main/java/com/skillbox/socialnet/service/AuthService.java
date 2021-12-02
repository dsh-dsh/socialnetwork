package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
//import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.security.CustomUserDetails;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.skillbox.socialnet.config.Config.checkPassword;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final PersonService personService;
    private final PersonModelMapper personModelMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public DefaultRS<UserDTO> login(AuthUserRQ authUserRQ) {
        Person person = personService.getPersonByEmail(authUserRQ.getEmail());
        if(!passwordEncoder.matches(authUserRQ.getPassword(), person.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(Constants.WRONG_CREDENTIALS_MESSAGE);
        }
        UserDTO userDTO = personModelMapper.mapToUserDTO(person);
        String token = jwtProvider.generateToken(person);
        userDTO.setToken(token);
        return DefaultRSMapper.of(userDTO);
    }


    public DefaultRS<MessageDTO> logout() {
        return DefaultRSMapper.of(new MessageDTO());
    }

    private UserDTO getUserDTO() {
        return new UserDTO();
        return DefaultRSMapper.of(userDTO);
    }

    public Person getPersonFromSecurityContext() {
        try{
            CustomUserDetails securityUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return personRepository.findByeMail((securityUser.getUsername()));
        } catch (Exception ex) {
            return null;
        }
    }


}
