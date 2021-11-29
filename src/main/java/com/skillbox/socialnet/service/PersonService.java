package com.skillbox.socialnet.service;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person getPersonByEmail(String email) {
        Person person = personRepository.findByeMail(email);
        if(person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        return person;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

}
