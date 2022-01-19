package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PlatformService platformService;

    public Person getPersonById(int id) {
        Person person = personRepository.findPersonById(id);
        if (person == null) {
            throw new NoSuchUserException(Constants.NO_SUCH_USER_MESSAGE);
        }
        return person;
    }

    public Person getPersonByEmail(String email) {
        return personRepository.findByeMail(email).orElseThrow(NoSuchUserException::new);
    }

    public Person editPerson(String email, UserChangeRQ userChangeRQ) {
        createLocations(userChangeRQ);
        Person person = getPersonByEmail(email);
        person.setFirstName(userChangeRQ.getFirstName());
        person.setLastName(userChangeRQ.getLastName());
        person.setBirthDate(new Timestamp(userChangeRQ.getBirthDate().getTime()));
        person.setBirthDate(userChangeRQ.getBirthDate());
        person.setPhone(userChangeRQ.getPhone());
        person.setAbout(userChangeRQ.getAbout());
        person.setMessagesPermission(userChangeRQ.getMessagesPermission());
        person.setCity(userChangeRQ.getCity());
        person.setCountry(userChangeRQ.getCountry());
        personRepository.save(person);
        return person;
    }

    private void createLocations(UserChangeRQ userChangeRQ) {
        String city = userChangeRQ.getCity();
        String country = userChangeRQ.getCountry();
        if(!city.equals("")) {
            platformService.setCity(new LocationDTO(0, city));
        }
        if(!country.equals("")) {
            platformService.setCountry(new LocationDTO(0, country));
        }
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Set<Person> getPersonsByIdList(List<Integer> ids) {
        return personRepository.findByIdIn(ids);
    }

}
