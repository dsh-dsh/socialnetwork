package com.skillbox.socialnet.service;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public UserDTO getUserDTOfromPerson(Person person){
        return UserDTO.builder()
                .id(person.getId())
                .about(person.getAbout())
//                .birthDate(person.getBirthDate().getTime())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEMail())
//                .registrationDate(person.getRegData().getTime())
                .phone(person.getPhone())
                .photo(person.getPhoto())
                .permission(person.getMessagesPermission())
//                .lastOnlineTime(person.getLastOnlineTime().getTime())
                .isBlocked(person.isBlocked())
                .city(person.getCity())
                .country(person.getCountry())
                .build();
    }
    
    public Person getPersonById(int id) {
        Person person = personRepository.getPersonById(id);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }

        return person;

    }

    public Person getPersonByEmail(String email) {

        Person person = personRepository.findByeMail(email);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        return person;
    }

    public Person editPerson(String email, UserChangeRQ userChangeRQ) {
        Person person = getPersonByEmail(email);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        person.setFirstName(userChangeRQ.getFirstName());
        person.setLastName(userChangeRQ.getLastName());
//        person.setBirthDate(new Timestamp(userChangeRQ.getBirthDate())); // TODO заменил в UserChangeRQ Long на Timestamp, потомучто фронт отправляет Timestamp
        person.setBirthDate(userChangeRQ.getBirthDate());
        person.setPhone(userChangeRQ.getPhone());
        person.setPhoto(userChangeRQ.getPhotoId());
        person.setAbout(userChangeRQ.getAbout());
        person.setMessagesPermission(userChangeRQ.getMessagesPermission());
        //TODO set location
        personRepository.save(person);
        return person;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

}
