package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.skillbox.socialnet.config.Config.checkPassword;

@Service
public class AuthService {

    private PersonRepository personRepository;

    public AuthService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public DefaultRS login(AuthUserRQ authUserRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        Person person = personRepository.findByeMail(authUserRQ.getEmail());
        if (person != null && checkPassword(person.getPassword(), authUserRQ.getPassword())){
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(person.getFirstName());
            userDTO.setLastName(person.getLastName());
            userDTO.setEmail(person.getEMail());
            defaultRS.setData(userDTO);
            return defaultRS;
        }
        defaultRS.setError("Invalid authentication!");
        return defaultRS;
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
