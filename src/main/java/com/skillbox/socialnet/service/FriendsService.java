package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonMapper;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final AuthService authService;

    public GeneralListResponse<?> getAllFriends(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> friendshipPage = friendshipRepository.findAllFriendsPageable(currentPerson, pageable);
        Stream<Person> personStream = friendshipPage.getContent().stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson());
        if (!name.equals("")) {
            personStream = personStream.filter(p -> p.getFirstName().equals(name));
        }
        List<UserDTO> friends = personStream.map(UserDTO::getUserDTO)
                    .collect(Collectors.toList());
        return new GeneralListResponse<>(friends, friendshipPage);
    }

    public MessageOkDTO deleteFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id).orElseThrow(BadRequestException::new);
        Friendship friendships = friendshipRepository.getRelationship(currentPerson, dstPerson)
                .orElseThrow(BadRequestException::new);
        friendshipRepository.delete(friendships);
        return new MessageOkDTO();
    }

    public MessageOkDTO addFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id)
                .orElseThrow(BadRequestException::new);
        if (!isFriends(currentPerson, dstPerson)) {
            List<Friendship> requests = friendshipRepository.findRequests(currentPerson, dstPerson);
            Friendship friendship = abs(currentPerson, dstPerson, requests);
            friendshipRepository.save(friendship);
        }
        return new MessageOkDTO();
    }

    public GeneralListResponse<?> getRequests(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> friendshipPage = friendshipRepository.findAllRequestPageable(currentPerson, pageable);
        Stream<Person> personStream = friendshipPage.getContent().stream().map(Friendship::getSrcPerson);
        if (!name.equals("")) {
            personStream = personStream.filter(p -> p.getFirstName().equals(name));
        }
        List<UserDTO> friends = personStream.map(UserDTO::getUserDTO).collect(Collectors.toList());
        return new GeneralListResponse<>(friends, friendshipPage);
    }

    public GeneralListResponse<?> getRecommendations(Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Set<Person> myFriends = friendshipRepository.findAllFriends(currentPerson)
                .stream().map(f -> getFriendFromFriendship(f, currentPerson))
                .collect(Collectors.toSet());

        List<UserDTO> recommendedFriendsList = new ArrayList<>();
        Set<Person> recommendedFriends = new HashSet<>();
        if(myFriends.size() > 0) {
            recommendedFriends = friendshipRepository.findAllFriendsOfMyFriends(myFriends)
                    .stream().filter(friendship -> !isMyFriendship(friendship, currentPerson))
                    .flatMap(friendship -> Stream.of(friendship.getDstPerson(), friendship.getSrcPerson()))
                    .filter(person -> !isFriends(person, currentPerson))
                    .collect(Collectors.toSet());
            recommendedFriendsList = recommendedFriends.stream()
                    .map(UserDTO::getUserDTO)
                    .collect(Collectors.toList());
        }
        if(recommendedFriendsList.size() < Constants.RECOMMENDED_FRIENDS_LIMIT) {
            int limit = Constants.RECOMMENDED_FRIENDS_LIMIT - recommendedFriendsList.size();
            Set<Person> personsToExclude = new HashSet<>();
            personsToExclude.addAll(myFriends);
            personsToExclude.addAll(recommendedFriends);
            personsToExclude.add(currentPerson);
            List<UserDTO> newFriends = personRepository.findNewFriendsLimit(personsToExclude, PageRequest.of(0, limit))
                    .stream().map(UserDTO::getUserDTO)
                    .collect(Collectors.toList());
            recommendedFriendsList.addAll(newFriends);
        }

        return new GeneralListResponse<>(recommendedFriendsList, pageable);
    }

    public List<StatusUserDTO> isMyFriends(List<Integer> userIds) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        return personRepository.findByIdIn(userIds).stream()
                .filter(p -> isFriends(currentPerson, p))
                .map(Person::getId)
                .map(StatusUserDTO::new)
                .collect(Collectors.toList());
    }

    public List<Person> getMyFriends() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson);
        return friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .collect(Collectors.toList());
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

    private boolean isFriends(Person currentPerson, Person dstPerson) {
        List<Friendship> friendships = friendshipRepository.isFriends(currentPerson, dstPerson);
        return !friendships.isEmpty();
    }

    private Friendship abs(Person currentPerson, Person dstPerson, List<Friendship> friendships) {
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

