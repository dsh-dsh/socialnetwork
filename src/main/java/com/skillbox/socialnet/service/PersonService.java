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


//    public Person getPersonByEmailAndPassword(String email, String password) {
//
//        return personRepository.findByeMailAndPassword(email, password)
//                .orElseThrow(() -> new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE));
//
//        Person person = Person.builder()
//                .id(1)
//                .firstName("user")
//                .eMail("user@email.com")
//                .password("$2a$12$xrqhhaGgQkVq9gsa59FEs.7jG40qwgdAxavIc8FA21c3kkhbwX8Ae")
//                .birthDate(Timestamp.valueOf(LocalDateTime.of(1973, 1, 23, 8, 30)))
//                .regData(Timestamp.valueOf(LocalDateTime.of(2021, 1, 1, 0,0)))
//                .lastOnlineTime(Timestamp.valueOf(LocalDateTime.of(2021, 10, 1, 0,0)))
//                .city("Krasnodar")
//                .messagesPermission(MessagesPermission.ALL)
//                .isBlocked(false)
//                .build();
//
//        System.out.println(password);
//
//        if(person.getEMail().equals(email) && person.getPassword().equals(password)) {
//            return person;
//        } else {
//            System.out.println("NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE)");
//            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
//        }
//
//    }

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
//        Person person = Person.builder()
//                .id(1)
//                .firstName("user")
//                .eMail("user@email.com")
//                .password("$2a$12$xrqhhaGgQkVq9gsa59FEs.7jG40qwgdAxavIc8FA21c3kkhbwX8Ae")
//                .birthDate(Timestamp.valueOf(LocalDateTime.of(1973, 1, 23, 8, 30)))
//                .regData(Timestamp.valueOf(LocalDateTime.of(2021, 1, 1, 0,0)))
//                .lastOnlineTime(Timestamp.valueOf(LocalDateTime.of(2021, 10, 1, 0,0)))
//                .city("Krasnodar")
//                .messagesPermission(MessagesPermission.ALL)
//                .isBlocked(false)
//                .build();
//
//        if(person.getEMail().equals(email)) {
//            return person;
//        }
//
//        return null;
    }



    public Person editPerson(String email, UserChangeRQ userChangeRQ) {
        Person person = getPersonByEmail(email);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        person.setFirstName(userChangeRQ.getFirstName());
        person.setLastName(userChangeRQ.getLastName());
        person.setBirthDate(new Timestamp(userChangeRQ.getBirthDate()));
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
