package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class PersonConverter {

    private AuthService authService;
    private FriendshipRepository friendshipRepository;
    private Person currentPerson;

    @Autowired
    public PersonConverter(AuthService authService, FriendshipRepository friendshipRepository) {
        this.authService = authService;
        this.friendshipRepository = friendshipRepository;
    }

    public UserDTO getUserDTO(Person person) {
        currentPerson = authService.getPersonFromSecurityContext();
        UserDTO userDTO = UserDTO.getUserDTO(person);
        userDTO.setMe(isMe(person));
        userDTO.setFriend(isFriend(person));
        userDTO.setBlocked(isBlocked(person));
        userDTO.setYouBlocked(isBlockedByPerson(person));
        return userDTO;
    }

    private Person getCurrentPerson() {
        if(currentPerson == null) {
            currentPerson = authService.getPersonFromSecurityContext();
        }
        return currentPerson;
    }

    private boolean isMe(Person opponentPerson) {
        return opponentPerson.equals(currentPerson);
    }

    private boolean isFriend(Person opponentPerson) {
        System.out.println(opponentPerson);
        System.out.println(getCurrentPerson());
        System.out.println("----------------");
        List<Friendship> friendshipList = friendshipRepository
                .findFriendshipsByStatusCode(getCurrentPerson(), opponentPerson, FriendshipStatusCode.FRIEND);
        return friendshipList.size() > 0;
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
