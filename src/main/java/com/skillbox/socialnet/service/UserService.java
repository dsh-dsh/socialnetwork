package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.rq.UserChangeRQ;
import com.skillbox.socialnet.model.rq.UserSearchRQ;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.skillbox.socialnet.util.Constants.USER_DELETE_SUCCESS;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final AuthService authService;

    public UserDTO getUser() {
        return UserDTO.getUserDTO(authService.getPersonFromSecurityContext());
    }

    public UserDTO getUserById(int id) {
        return UserDTO.getUserDTO(personService.getPersonById(id));
    }

    public UserDTO editUser(UserChangeRQ userChangeRQ) {
        return UserDTO.getUserDTO(personService.editPerson(authService.getPersonFromSecurityContext().getEMail(),
                userChangeRQ));
    }

    public String deleteUser() {
        personRepository.delete(authService.getPersonFromSecurityContext());
        return USER_DELETE_SUCCESS;
    }

    public GeneralListResponse<UserDTO> searchUsers(String firstOrLastName, Pageable pageable) {
        Page<Person> personPage = personRepository
                .findByFirstNameContainingOrLastNameContainingIgnoreCase(firstOrLastName, firstOrLastName, pageable);
        List<UserDTO> userDTOList = personPage.stream()
                .map(UserDTO::getUserDTO).collect(Collectors.toList());
        return new GeneralListResponse<>(userDTOList, personPage);
    }

    @Loggable
    public GeneralListResponse<UserDTO> searchUsers(UserSearchRQ userSearchRQ, ElementPageable pageable) {
        Date to = getDateTo(userSearchRQ);
        Date from = getDateFrom(userSearchRQ);
        userSearchRQ.firstNameToLower();
        userSearchRQ.lastNameToLower();
        Page<Person> personPage = personRepository.findBySearchRequest(
                userSearchRQ.getFirstName(), userSearchRQ.getLastName(),
                userSearchRQ.getCountry(), userSearchRQ.getCity(), from, to, pageable);
        List<UserDTO> userDTOList = personPage.stream()
                .map(UserDTO::getUserDTO).collect(Collectors.toList());
        return new GeneralListResponse<>(userDTOList, personPage);
    }

    public MessageOkDTO blockUser(int id) {
        Person person = personService.getPersonById(id);
        person.setBlocked(true);
        personRepository.save(person);
        return new MessageOkDTO();
    }

    public MessageOkDTO unblockUser(int id) {
        Person person = personService.getPersonById(id);
        person.setBlocked(false);
        personRepository.save(person);
        return new MessageOkDTO();
    }

    public MessageOkDTO checkOnline() {
        Person me = authService.getPersonFromSecurityContext();
        me.setLastOnlineTime(new Timestamp(new Date().getTime()));
        personRepository.save(me);
        return new MessageOkDTO();
    }


    private Date getDateFrom(UserSearchRQ userSearchRQ) {
        int ageTo = userSearchRQ.getAgeTo() == 0 ? Constants.MAX_AGE : userSearchRQ.getAgeTo();
        return Date.from(LocalDate.now().minusYears(ageTo).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getDateTo(UserSearchRQ userSearchRQ) {
        int ageFrom = userSearchRQ.getAgeFrom() - 1;
        return Date.from(LocalDate.now().minusYears(ageFrom).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
