package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.model.mapper.PersonMapper;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final NotificationService notificationService;
    private final PersonService personService;
    private final PersonMapper personMapper;

    public GeneralListResponse<UserDTO> getAllFriends(ElementPageable pageable) {
        pageable.setSort(Sort.unsorted());
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> friendshipPage = friendshipRepository
                .findAllFriendsPageable(currentPerson, pageable);
        List<Person> persons = getFriendsFromFriendships(currentPerson, friendshipPage.getContent());
        List<UserDTO> userDTOList = getUserDTOList(persons);

        return new GeneralListResponse<>(userDTOList, friendshipPage);
    }

    public GeneralListResponse<UserDTO> getRequests(ElementPageable pageable) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Page<Friendship> requestPage = friendshipRepository
                .findAllRequestsPageable(currentPerson, FriendshipStatusCode.REQUEST, pageable);
        List<Person> persons = requestPage.getContent().stream()
                .map(Friendship::getSrcPerson).collect(Collectors.toList());
        List<UserDTO> userDTOList = getUserDTOList(persons);

        return new GeneralListResponse<>(userDTOList, requestPage);
    }

    public MessageOkDTO deleteFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(id);
        Friendship friendships = friendshipRepository.getRelationship(currentPerson, dstPerson)
                .orElseThrow(BadRequestException::new);
        friendshipRepository.delete(friendships);

        return new MessageOkDTO();
    }

    public MessageOkDTO addFriend(int id) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(id);
        if (!isFriends(currentPerson, dstPerson)) {
            Friendship friendship = getAndAcceptFriendship(currentPerson, dstPerson);
            if(friendship == null) {
                friendship = createFriendship(currentPerson, dstPerson, FriendshipStatusCode.REQUEST);
                notificationService.createNewNotification(
                        NotificationTypeCode.FRIEND_REQUEST,
                        dstPerson.getId(),
                        currentPerson.getId(),
                        dstPerson.getEMail());
            }
            friendshipRepository.save(friendship);
        }

        return new MessageOkDTO();
    }

    private Friendship getAndAcceptFriendship(Person currentPerson, Person dstPerson) {
        List<Friendship> requests = friendshipRepository
                .findRequests(currentPerson, dstPerson, FriendshipStatusCode.REQUEST);
        if(!requests.isEmpty()) {
            return acceptFriendship(dstPerson, requests);
        }

        return null;
    }

    private Friendship acceptFriendship(Person dstPerson, List<Friendship> requests) {
        Friendship request = requests.get(0);
        if(request.getSrcPerson() == dstPerson) {
            FriendshipStatus status = request.getStatus();
            status.setCode(FriendshipStatusCode.FRIEND);
            status.setName(FriendshipStatusCode.FRIEND.getName());
            status.setTime(LocalDateTime.now());
        }

        return request;
    }

    public List<UserDTO> getRecommendations() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Set<Person> myFriends = friendshipRepository.findAllFriendsIncludeBlocked(currentPerson)
                .stream().map(f -> getFriendFromFriendship(f, currentPerson))
                .collect(Collectors.toSet());
        Set<Person> recommendedFriends = getRecommendedFriends(currentPerson, myFriends);

        return getUserDTOList(recommendedFriends);
    }

    public Set<Person> getRecommendedFriends(Person currentPerson, Set<Person> myFriends) {
        Set<Person> recommendedFriends = new HashSet<>();
        if(!myFriends.isEmpty()) {
            recommendedFriends = friendshipRepository
                    .findAllFriendsOfMyFriends(myFriends, FriendshipStatusCode.FRIEND)
                    .stream().filter(friendship -> !isMyFriendship(friendship, currentPerson))
                    .flatMap(friendship -> Stream.of(friendship.getDstPerson(), friendship.getSrcPerson()))
                    .filter(person -> !myFriends.contains(person))
                    .collect(Collectors.toSet());
        }
        addNewFriendsToRecommended(currentPerson, myFriends, recommendedFriends);

        return recommendedFriends;
    }

    private void addNewFriendsToRecommended(Person currentPerson, Set<Person> myFriends, Set<Person> recommendedFriends) {
        int limit = Constants.RECOMMENDED_FRIENDS_LIMIT - recommendedFriends.size();
        if(limit > 0) {
            Set<Person> personsToExclude = getPersonsToExclude(currentPerson, myFriends, recommendedFriends);
            List<Person> newFriends = personRepository.findNewFriendsLimit(personsToExclude, PageRequest.of(0, limit));
            recommendedFriends.addAll(newFriends);
        }
    }

    public MessageOkDTO blockUser(int personId) {
        Person srcPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(personId);
        Friendship friendship = friendshipRepository
                .findFriendshipByStatusCode(srcPerson, dstPerson, FriendshipStatusCode.FRIEND)
                .orElseGet(() -> createFriendship(srcPerson, dstPerson, FriendshipStatusCode.BLOCKED));
        friendship.getStatus().setCode(FriendshipStatusCode.BLOCKED);
        friendship.getStatus().setName(FriendshipStatusCode.BLOCKED.getName());
        friendshipRepository.save(friendship);

        return new MessageOkDTO();
    }

    public MessageOkDTO unblockUser(int personId) {
        Person srcPerson = authService.getPersonFromSecurityContext();
        Person dstPerson = personService.getPersonById(personId);
        List<Friendship> friendships = friendshipRepository.findFriendships(srcPerson, dstPerson);
        unblockOrDeleteFriendship(srcPerson, friendships);

        return new MessageOkDTO();
    }

    private void unblockOrDeleteFriendship(Person srcPerson, List<Friendship> friendships) {
        for(Friendship friendship : friendships) {
            if(friendship.getSrcPerson().equals(srcPerson)) {
                if(friendships.size() > 1) {
                    friendshipRepository.delete(friendship);
                } else {
                    friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
                    friendship.getStatus().setName(FriendshipStatusCode.FRIEND.getName());
                    friendshipRepository.save(friendship);
                }
            }
        }
    }

    public List<Person> getBlockedFriends(Person currentPerson) {
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.BLOCKED);
        return getFriendsFromFriendships(currentPerson, friendships);
    }

    private Set<Person> getPersonsToExclude(Person currentPerson, Set<Person> myFriends, Set<Person> recommendedFriends) {
        Set<Person> personsToExclude = new HashSet<>();
        personsToExclude.addAll(myFriends);
        personsToExclude.addAll(recommendedFriends);
        personsToExclude.add(currentPerson);

        return personsToExclude;
    }

    public List<Person> getMyNotBlockedFriends() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.FRIEND);
        List<Person> friends = friendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .distinct()
                .collect(Collectors.toList());
        List<Friendship> blockedFriendships = friendshipRepository.findAllFriends(currentPerson, FriendshipStatusCode.BLOCKED);
        List<Person> blockedFriends = blockedFriendships.stream()
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .collect(Collectors.toList());
        friends.removeAll(blockedFriends);
        return friends;
    }

    private Friendship createFriendship(Person currentPerson, Person dstPerson, FriendshipStatusCode statusCode) {
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(currentPerson);
        friendship.setDstPerson(dstPerson);
        friendship.setStatus(createFriendshipStatus(statusCode));

        return friendship;
    }

    private FriendshipStatus createFriendshipStatus(FriendshipStatusCode statusCode) {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setName(statusCode.getName());
        friendshipStatus.setCode(statusCode);

        return friendshipStatus;
    }

    private List<Person> getFriendsFromFriendships(Person currentPerson, List<Friendship> friendships) {
        return friendships.stream()
                .map(friendship -> getFriendFromFriendship(friendship, currentPerson))
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isMyFriendship(Friendship friendship, Person currentPerson) {
        return !(friendship.getSrcPerson() != currentPerson && friendship.getDstPerson() != currentPerson);
    }

    private static Person getFriendFromFriendship(Friendship friendship, Person currentPerson) {
        if (friendship.getSrcPerson() == currentPerson) {
            return friendship.getDstPerson();
        }
        if (friendship.getDstPerson() == currentPerson) {
            return friendship.getSrcPerson();
        }
        return null;
    }

    public boolean isFriends(Person currentPerson, Person dstPerson) {
        List<Friendship> friendships = friendshipRepository
                .findFriendshipsByStatusCode(currentPerson, dstPerson, FriendshipStatusCode.FRIEND);

        return !friendships.isEmpty();
    }

    private List<UserDTO> getUserDTOList(Collection<Person> persons) {
        return persons.stream().map(personMapper::mapToUserDTO).collect(Collectors.toList());
    }

}

