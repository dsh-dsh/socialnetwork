package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final NotificationService notificationService;

    public GeneralListResponse<UserDTO> getAllFriends(String name, ElementPageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> friendshipPage = friendshipRepository
                .findAllFriendsPageable(currentPerson, pageable);
        List<Person> persons = getFriendsFromFriendships(name, currentPerson, friendshipPage.getContent());
        List<UserDTO> userDTOList = getUserDTOList(persons);

        return new GeneralListResponse<UserDTO>(userDTOList, friendshipPage);
    }

    public GeneralListResponse<UserDTO> getRequests(String name, ElementPageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> requestPage = friendshipRepository
                .findAllRequestsPageable(currentPerson, FriendshipStatusCode.REQUEST, pageable);
        List<Person> persons = getFriendsFromRequests(name, requestPage.getContent());
        List<UserDTO> userDTOList = getUserDTOList(persons);

        return new GeneralListResponse<UserDTO>(userDTOList, requestPage);
    }

    private List<Person> getFriendsFromRequests(String name, List<Friendship> requests) {
        Stream<Person> personStream = requests.stream()
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
        notificationService.createNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                dstPerson.getId(),
                NotificationTypeCode.FRIEND_REQUEST.ordinal(),
                dstPerson.getEMail());

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

    public List<UserDTO> getRecommendations() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Set<Person> myFriends = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.FRIEND)
                .stream().map(f -> getFriendFromFriendship(f, currentPerson))
                .collect(Collectors.toSet());
        Set<Person> recommendedFriends = getRecommendedFriends(currentPerson, myFriends);

        return getUserDTOList(recommendedFriends);
    }

    public Set<Person> getRecommendedFriends(Person currentPerson, Set<Person> myFriends) {
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
        if(limit > 0) {
            Set<Person> personsToExclude = getPersonsToExclude(currentPerson, myFriends, recommendedFriends);
            List<Person> newFriends = personRepository.findNewFriendsLimit(personsToExclude, PageRequest.of(0, limit));
            recommendedFriends.addAll(newFriends);
        }

        return recommendedFriends;
    }

    public MessageOkDTO blockUser(int personId) {
        Person srcPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(personId);
        Friendship friendship = friendshipRepository
                .findFriendship(srcPerson, dstPerson, FriendshipStatusCode.FRIEND)
                .orElseThrow(() -> new BadRequestException(Constants.NO_SUCH_FRIENDSHIP_MESSAGE));
        if(friendship.getSrcPerson().equals(dstPerson)) {
            friendship.setSrcPerson(srcPerson);
            friendship.setDstPerson(dstPerson);
        }
        friendship.getStatus().setCode(FriendshipStatusCode.BLOCKED);
        friendshipRepository.save(friendship);

        return new MessageOkDTO();
    }

    public MessageOkDTO unblockUser(int personId) {
        Person srcPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(personId);
        Friendship friendship = friendshipRepository
                .findBySrcPersonAndDstPerson(srcPerson, dstPerson)
                .orElseThrow(() -> new BadRequestException(Constants.NO_SUCH_FRIENDSHIP_MESSAGE));
        friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        friendshipRepository.save(friendship);

        return new MessageOkDTO();
    }

    private Set<Person> getPersonsToExclude(Person currentPerson, Set<Person> myFriends, Set<Person> recommendedFriends) {
        Set<Person> personsToExclude = new HashSet<>();
        personsToExclude.addAll(myFriends);
        personsToExclude.addAll(recommendedFriends);
        personsToExclude.add(currentPerson);

        return personsToExclude;
    }

    public List<Person> getMyFriends() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.FRIEND);
        return friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .collect(Collectors.toList());
    }

    private Friendship createFriendshipRequest(Person currentPerson, Person dstPerson) {
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(currentPerson);
        friendship.setDstPerson(dstPerson);
        friendship.setStatus(createFriendshipStatus(FriendshipStatusCode.FRIEND));
        notificationService.createNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                dstPerson.getId(),
                currentPerson.getId(),
                dstPerson.getEMail());
        return friendship;
    }


    private FriendshipStatus createFriendshipStatus(FriendshipStatusCode statusCode) {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
//        friendshipStatus.setName("Request");
        friendshipStatus.setCode(statusCode);

        return friendshipStatus;
    }

    private List<UserDTO> getUserDTOList(Collection<Person> persons) {
        return persons.stream().map(UserDTO::getUserDTO).collect(Collectors.toList());
    }

    private List<Person> getFriendsFromFriendships(String name, Person currentPerson, List<Friendship> friendships) {
        Stream<Person> personStream = friendships.stream()
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

    public boolean isFriends(Person currentPerson, Person dstPerson) {
        List<Friendship> friendships = friendshipRepository
                .isFriends(currentPerson, dstPerson, FriendshipStatusCode.FRIEND);

        return !friendships.isEmpty();
    }

}

