package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final AuthService authService;
    private final FriendshipRepository friendshipRepository;

    private ModelMapper modelMapper;
    private Person currentPerson;

    private final Converter<Timestamp, Long> timestampConverter =
            date -> {
                Timestamp timestamp = date.getSource();
                return timestamp == null? 0 : timestamp.getTime();
            };

    private Converter<Integer, Boolean> meConverter;
    private Converter<Person, Boolean> isFriendConverter;
    private Converter<Person, Boolean> isBlockedConverter;
    private Converter<Person, Boolean> isYouBlockedConverter;

    @PostConstruct
    public void init() {

        modelMapper = new ModelMapper();

        meConverter = id -> id.getSource() == authService.getPersonFromSecurityContext().getId();
        isFriendConverter = person -> isFriend(person.getSource());
        isBlockedConverter = person -> isBlocked(person.getSource());
        isYouBlockedConverter = person -> isBlockedByPerson(person.getSource());

        modelMapper.createTypeMap(Person.class, UserDTO.class)
                .addMappings(m -> m.using(isYouBlockedConverter).map(Person::getThis, UserDTO::setYouBlocked))
                .addMappings(m -> m.using(timestampConverter).map(Person::getRegDate, UserDTO::setRegistrationDate))
                .addMappings(m -> m.using(timestampConverter).map(Person::getBirthDate, UserDTO::setBirthDate))
                .addMappings(m -> m.using(timestampConverter).map(Person::getLastOnlineTime, UserDTO::setLastOnlineTime))
                .addMappings(m -> m.using(meConverter).map(Person::getId, UserDTO::setMe))
                .addMappings(m -> m.using(isFriendConverter).map(Person::getThis, UserDTO::setFriend))
                .addMappings(m -> m.using(isBlockedConverter).map(Person::getThis, UserDTO::setBlocked));
    }
    
    public UserDTO mapToUserDTO(Person person) {
        currentPerson = authService.getPersonFromSecurityContext();
        return modelMapper.map(person, UserDTO.class);
    }

    private Person getCurrentPerson() {
        if(currentPerson == null) {
            currentPerson = authService.getPersonFromSecurityContext();
        }
        return currentPerson;
    }

    private boolean isFriend(Person opponentPerson) {
        List<Friendship> friendshipList = friendshipRepository
                .findFriendships(getCurrentPerson(), opponentPerson);
        return !friendshipList.isEmpty();
    }

    private boolean isBlocked(Person opponentPerson) {
        Optional<Friendship> friendship = friendshipRepository
                .findFriendshipByStatusCode(getCurrentPerson(), opponentPerson, FriendshipStatusCode.BLOCKED);
        return friendship.isPresent();
    }

    private boolean isBlockedByPerson(Person opponentPerson) {
        Optional<Friendship> friendship = friendshipRepository
                .findFriendshipByStatusCode(opponentPerson, getCurrentPerson(), FriendshipStatusCode.BLOCKED);
        return friendship.isPresent();
    }
}
