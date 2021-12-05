package com.skillbox.socialnet.service;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person getPersonById(int id) {
        Person person = personRepository.findPersonById(id);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        return person;
    }

    public Person getPersonByEmail(String email) {
        return personRepository.findByeMail(email).orElseThrow(NoSuchUserException::new);
        // FIXME не правильно отрабатал при смене имейла(при ошибке фронта с токеном) ошибка 500,
        //  проверить когда исправим фронт
        //  при переходе из письма для смены почты открывается новая вкладка
        //  и там смена почты отрабатывает нормально
        //  но предыдущая вкладка продолжает отправлять запросы со старым токеном
        //  и соответственно со старым емейлом
    }

    public Person editPerson(String email, UserChangeRQ userChangeRQ) {
        Person person = getPersonByEmail(email);
        if (person == null) { // TODO можно удалить в getPersonByEmail(email) уже проверяется
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        person.setFirstName(userChangeRQ.getFirstName());
        person.setLastName(userChangeRQ.getLastName());
        person.setBirthDate(new Timestamp(userChangeRQ.getBirthDate().getTime()));
        person.setBirthDate(userChangeRQ.getBirthDate());
        person.setPhone(userChangeRQ.getPhone());
//        person.setPhoto(userChangeRQ.getPhotoId());
        person.setAbout(userChangeRQ.getAbout());
        person.setMessagesPermission(userChangeRQ.getMessagesPermission());
        person.setCity(userChangeRQ.getCity());
        person.setCountry(userChangeRQ.getCountry());
        personRepository.save(person);
        return person;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

}
