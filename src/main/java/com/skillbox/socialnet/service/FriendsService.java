package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.util.anotation.MethodLog;
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

    public GeneralListResponse<UserDTO> getAllFriends(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> friendshipPage = friendshipRepository
                .findAllFriendsPageable(currentPerson, FriendshipStatusCode.FRIEND, pageable);
        List<Person> personStream = getFriendsFromFriendshipPage(name, currentPerson, friendshipPage);
        List<UserDTO> friends = getUserDTOList(personStream);

        return new GeneralListResponse<>(friends, friendshipPage);
    }

    public GeneralListResponse<?> getRequests(String name, Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> requestsPage = friendshipRepository
                .findAllRequestPageable(currentPerson, FriendshipStatusCode.REQUEST, pageable);
        List<Person> personStream = getFriendsFromRequestPage(name, currentPerson, requestsPage);
        List<UserDTO> friends = getUserDTOList(personStream);

        return new GeneralListResponse<>(friends, requestsPage);
    }

    private List<Person> getFriendsFromRequestPage(String name, Person currentPerson, Page<Friendship> requestsPage) {
        Stream<Person> personStream = requestsPage.getContent().stream()
                .map(Friendship::getSrcPerson);
        if (!name.equals("")) {
            personStream = personStream.filter(person -> person.getFirstName().equals(name));
        }

        return personStream.collect(Collectors.toList());
    }

    public MessageOkDTO deleteFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personRepository.getPersonById(id)
                .orElseThrow(BadRequestException::new);
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
            Friendship friendship = getAndAcceptFriendship(currentPerson, dstPerson)
                    .orElseGet(() -> createFriendshipRequest(currentPerson, dstPerson));
            friendshipRepository.save(friendship);
        }

        return new MessageOkDTO();
    }

    private Optional<Friendship> getAndAcceptFriendship(Person currentPerson, Person dstPerson) {
        List<Friendship> requests = friendshipRepository
                .findRequests(currentPerson, dstPerson, FriendshipStatusCode.REQUEST);
        if(requests.size() > 0) {
            Friendship friendship = acceptFriendship(dstPerson, requests);
            return Optional.of(friendship);
        }

        return Optional.empty();
    }

    private Friendship acceptFriendship(Person dstPerson, List<Friendship> requests) {
        Friendship request = requests.get(0);
        if(request.getSrcPerson() == dstPerson) {
            FriendshipStatus status = request.getStatus();
            status.setCode(FriendshipStatusCode.FRIEND);
            status.setName("Friend");
            status.setTime(LocalDateTime.now());
        }

        return request;
    }

    private Friendship createFriendshipRequest(Person currentPerson, Person dstPerson) {
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(currentPerson);
        friendship.setDstPerson(dstPerson);
        friendship.setStatus(createRequestFriendshipStatus());

        return friendship;
    }

    public GeneralListResponse<?> getRecommendations(Pageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Set<Person> myFriends = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.FRIEND)
                .stream().map(f -> getFriendFromFriendship(f, currentPerson))
                .collect(Collectors.toSet());
        Set<Person> recommendedFriends = getRecommendedFriends(currentPerson, myFriends);
        List<UserDTO> recommendedFriendsList = getUserDTOList(recommendedFriends);

        return new GeneralListResponse<>(recommendedFriendsList, pageable);
    }

    private Set<Person> getRecommendedFriends(Person currentPerson, Set<Person> myFriends) {
        Set<Person> recommendedFriends = new HashSet<>();
        if(myFriends.size() > 0) {
            recommendedFriends = friendshipRepository
                    .findAllFriendsOfMyFriends(myFriends, FriendshipStatusCode.FRIEND)
                    .stream().filter(friendship -> !isMyFriendship(friendship, currentPerson))
                    .flatMap(friendship -> Stream.of(friendship.getDstPerson(), friendship.getSrcPerson()))
                    .filter(person -> !isFriends(person, currentPerson))
                    .collect(Collectors.toSet());
        }
        int limit = Constants.RECOMMENDED_FRIENDS_LIMIT - recommendedFriends.size();
        Set<Person> personsToExclude = getPersonsToExclude(currentPerson, myFriends, recommendedFriends);
        List<Person> newFriends = personRepository.findNewFriendsLimit(personsToExclude, PageRequest.of(0, limit));
        recommendedFriends.addAll(newFriends);

        return recommendedFriends;
    }

    private Set<Person> getPersonsToExclude(Person currentPerson, Set<Person> myFriends, Set<Person> recommendedFriends) {
        Set<Person> personsToExclude = new HashSet<>();
        personsToExclude.addAll(myFriends);
        personsToExclude.addAll(recommendedFriends);
        personsToExclude.add(currentPerson);

        return personsToExclude;
    }

    @MethodLog
    public List<Person> getMyFriends() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.FRIEND);
        return friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .collect(Collectors.toList());
    }

    private FriendshipStatus createRequestFriendshipStatus() {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setName("Request");
        friendshipStatus.setCode(FriendshipStatusCode.REQUEST);

        return friendshipStatus;
    }

    private List<UserDTO> getUserDTOList(Collection<Person> persons) {
        return persons.stream().map(UserDTO::getUserDTO).collect(Collectors.toList());
    }

    private List<Person> getFriendsFromFriendshipPage(String name, Person currentPerson, Page<Friendship> friendshipPage) {
        Stream<Person> personStream = friendshipPage.getContent().stream()
                .map(friendship -> friendship
                        .getSrcPerson().equals(currentPerson) ?
                        friendship.getDstPerson() :
                        friendship.getSrcPerson());
        if (!name.equals("")) {
            personStream = personStream.filter(person -> person.getFirstName().equals(name));
        }

        return personStream.collect(Collectors.toList());
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
        List<Friendship> friendships = friendshipRepository
                .isFriends(currentPerson, dstPerson, FriendshipStatusCode.FRIEND);

        return !friendships.isEmpty();
    }

}

