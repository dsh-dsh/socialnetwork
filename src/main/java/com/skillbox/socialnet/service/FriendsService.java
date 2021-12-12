package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final PersonModelMapper personModelMapper;
    private final AuthService authService;

    public DefaultRS getAllFriends(String name, Pageable pageable) {

        Person currentPerson = authService.getPersonFromSecurityContext();

        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson);
        List<UserDTO> friends;
        Stream<Person> personStream = friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson());

        if (name.equals("")) {

            friends = personStream.map(personModelMapper::mapToUserDTO).collect(Collectors.toList());
        } else {
            friends = personStream.filter(p -> p.getFirstName().equals(name)).map(personModelMapper::mapToUserDTO).collect(Collectors.toList());
        }

        return DefaultRSMapper.of(friends, pageable);
    }

    public DefaultRS deleteFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id).orElseThrow(BadRequestException::new);

        Friendship friendships = friendshipRepository.getRelationship(currentPerson, dstPerson).orElseThrow(BadRequestException::new);
        friendshipRepository.delete(friendships);

        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS addFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id).orElseThrow(BadRequestException::new);

        DefaultRS defaultRS = DefaultRSMapper.of(new MessageDTO());

        if (isFriend(currentPerson, dstPerson))
            return defaultRS;

        List<Friendship> friendships = friendshipRepository.requests(currentPerson, dstPerson);

        Friendship friendship = abs(currentPerson, dstPerson, friendships);

        friendshipRepository.save(friendship);

        return defaultRS;
    }

    public DefaultRS getRequests(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();

        List<Friendship> friendships = friendshipRepository.findAllRequest(currentPerson);
        Stream<Person> personStream = friendships.stream().map(Friendship::getSrcPerson);

        List<UserDTO> friends;
        if (name.equals("")) {
            friends = personStream.map(personModelMapper::mapToUserDTO).collect(Collectors.toList());
        } else {
            friends = personStream.filter(p -> p.getFirstName().equals(name))
                    .map(personModelMapper::mapToUserDTO).collect(Collectors.toList());
        }

        return DefaultRSMapper.of(friends, pageable);
    }

//    public DefaultRS getRecommendations(Pageable pageable) {
//        Person currentPerson = getCurrentPerson();
//        if(currentPerson == null)
//            return DefaultRSMapper.error("invalid_request");
//
//        //TODO лютая заплатка, создать метод в FriendshipRepository, переписать на sql запрос
//        List<UserDTO> recommendations =
//                friendshipRepository.findAllFriends(currentPerson).stream()
//                        .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
//                        .map(p -> friendshipRepository.findAllFriends(p).stream()
//                                .map(f -> f.getSrcPerson().equals(p) ? f.getDstPerson() : f.getSrcPerson()).collect(Collectors.toList()))
//                        .flatMap(l -> l.stream()).filter(p -> !p.equals(currentPerson)).map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
//        /////
//
//        DefaultRS defaultRS = DefaultRSMapper.of(recommendations, pageable);
//
//        return defaultRS;
//    }

    public DefaultRS<?> getRecommendations(Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();

        Page<Person> personPage = personRepository.findAll(pageable);

        List<UserDTO> friends = personPage.stream()
                .filter(person -> !person.getEMail().equals(currentPerson.getEMail()))
                .map(personModelMapper::mapToUserDTO)
                .collect(Collectors.toList());

        return DefaultRSMapper.of(friends, personPage);
    }

    public DefaultRS isFriends(List<Integer> userIds) {
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

        return friendships.isEmpty() ? false : true;
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

