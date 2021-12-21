package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonMapper;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final AuthService authService;

    public DefaultRS<?> getAllFriends(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson);
        Stream<Person> personStream = friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson());
        List<UserDTO> friends;
        if (name.equals("")) {
            friends = personStream.map(personMapper::mapToUserDTO)
                    .collect(Collectors.toList());
        } else {
            friends = personStream.filter(p -> p.getFirstName().equals(name))
                    .map(personMapper::mapToUserDTO).collect(Collectors.toList());
        }
        return DefaultRSMapper.of(friends, pageable);
    }

    public List<Person> getMyFriends() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson);
        return friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .collect(Collectors.toList());
    }

    public DefaultRS<?> deleteFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id).orElseThrow(BadRequestException::new);
        Friendship friendships = friendshipRepository.getRelationship(currentPerson, dstPerson).orElseThrow(BadRequestException::new);
        friendshipRepository.delete(friendships);
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> addFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id).orElseThrow(BadRequestException::new);
        DefaultRS<?> defaultRS = DefaultRSMapper.of(new MessageDTO());
        if (isFriend(currentPerson, dstPerson)) {
            return defaultRS;
        }
        List<Friendship> friendships = friendshipRepository.requests(currentPerson, dstPerson);
        Friendship friendship = abs(currentPerson, dstPerson, friendships);
        friendshipRepository.save(friendship);
        return defaultRS;
    }

    public DefaultRS<?> getRequests(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllRequest(currentPerson);
        Stream<Person> personStream = friendships.stream().map(Friendship::getSrcPerson);
        List<UserDTO> friends;
        if (name.equals("")) {
            friends = personStream.map(personMapper::mapToUserDTO).collect(Collectors.toList());
        } else {
            friends = personStream.filter(p -> p.getFirstName().equals(name))
                    .map(personMapper::mapToUserDTO).collect(Collectors.toList());
        }
        return DefaultRSMapper.of(friends, pageable);
    }

    public DefaultRS<?> getRecommendations(Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> myFriendships = friendshipRepository.findAllFriends(currentPerson);

        List<UserDTO> recommendedFriendsList = new ArrayList<>();
        if(myFriendships.size() > 0) {
            List<Person> myFriends = myFriendships.stream()
                    .map(f -> getFriendFromFriendship(f, currentPerson))
                    .collect(Collectors.toList());

            List<Friendship> friendsFriendships = friendshipRepository.findAllFriendsOfMyFriends(myFriends);

            Set<Person> recommendedFriends = friendsFriendships.stream()
                    .filter(friendship -> !isMyFriendship(friendship, currentPerson))
                    .flatMap(friendship -> Stream.of(friendship.getDstPerson(), friendship.getSrcPerson()))
                    .filter(person -> !isFriend(person, currentPerson))
                    .collect(Collectors.toSet());

            recommendedFriendsList = recommendedFriends.stream()
                    .map(personMapper::mapToUserDTO)
                    .collect(Collectors.toList());
        }
        return DefaultRSMapper.of(recommendedFriendsList, pageable);
    }

    private boolean isMyFriendship(Friendship friendship, Person currentPerson) {
        return !(friendship.getSrcPerson() != currentPerson && friendship.getDstPerson() != currentPerson);
    }

    private Person getFriendFromFriendship(Friendship friendship, Person currentPerson) {
        if (!isMyFriendship(friendship, currentPerson)) {
            return null;
        }
        if (friendship.getSrcPerson() == currentPerson) {
            return friendship.getDstPerson();
        } else {
            return friendship.getSrcPerson();
        }
    }

    public DefaultRS<?> isFriends(List<Integer> userIds) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<StatusUserDTO> friends = personRepository.findByIdIn(userIds).stream()
                .filter(p -> isFriend(currentPerson, p))
                .map(Person::getId)
                .map(StatusUserDTO::new)
                .collect(Collectors.toList());
        return DefaultRSMapper.of(friends);
    }

    private boolean isFriend(Person currentPerson, Person dstPerson) {
        List<Friendship> friendships = friendshipRepository.isFriends(currentPerson, dstPerson);
        return !friendships.isEmpty();
    }

    private Friendship abs(Person currentPerson, Person dstPerson,List<Friendship> friendships){
        Friendship friendship = new Friendship();
        if (friendships.isEmpty()) {
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setName("Request");
            friendshipStatus.setCode(FriendshipStatusCode.REQUEST);
            friendship.setSrcPerson(currentPerson);
            friendship.setDstPerson(dstPerson);
            friendship.setStatus(friendshipStatus);
        } else {
            friendship = friendships.get(0);
            friendship.getStatus().setTime(LocalDateTime.now());
            friendship.getStatus().setName("Friend");
            friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        }
        return friendship;
    }

}

