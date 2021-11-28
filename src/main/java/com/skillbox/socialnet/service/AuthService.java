package com.skillbox.socialnet.service;

import com.skillbox.socialnet.Constants;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
//import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.skillbox.socialnet.config.Config.checkPassword;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final PersonService personService;
//    private final PersonModelMapper personModelMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public DefaultRS<UserDTO> loginJwt(AuthUserRQ authUserRQ) {
        Person person = personService.getPersonByEmail(authUserRQ.getEmail());
        if(!passwordEncoder.matches(authUserRQ.getPassword(), person.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(Constants.WRONG_CREDENTIALS_MESSAGE);
        }
        UserDTO userDTO = personService.getUserDTOfromPerson(person);
        String token = jwtProvider.generateToken(person);
        userDTO.setToken(token);
        return Utils.defaultRS(userDTO);
    }


    public DefaultRS logout() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    private UserDTO getUserDTO() {
        return new UserDTO();
    }




}
