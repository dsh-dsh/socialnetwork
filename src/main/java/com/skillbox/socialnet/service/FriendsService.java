package com.skillbox.socialnet.service;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
    private final PersonModelMapper personModelMapper;

     public DefaultRS getAllFriends(String name, int offset, int itemPerPage) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());

        List<Friendship> friendships = friendshipRepository.findAllFriends(currentPerson);
        List<UserDTO> friends;
        Stream<Person> personStream = friendships.stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipStatusCode.FRIEND))
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson());

        if(name.equals("")){
            friends = personStream.map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
        }else{
            friends = personStream.filter(p -> p.getFirstName().equals(name)).map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
        }

        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setData(friends);

        return defaultRS;
    }

    public DefaultRS deleteFriend(int id) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());
        Person dstPerson = personRepository.getPersonById(id);
        List<Friendship> friendships = friendshipRepository.isFriends(currentPerson,dstPerson);
        Optional<Friendship> friendshipOptional = friendships.stream().filter(f -> f.getSrcPerson().equals(dstPerson) || f.getDstPerson().equals(dstPerson)).findFirst();
        if(friendshipOptional.isPresent()){
            Friendship friendship = friendshipOptional.get();
            friendshipRepository.delete(friendship);
        }

        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS addFriend(int id) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());
        Person dstPerson = personRepository.getPersonById(id);

        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());

        if(isFriend(currentPerson,dstPerson))
            return defaultRS;

        List<Friendship> friendships = friendshipRepository.findRequestFromDstPersonToSrcPerson(currentPerson,dstPerson);

        Friendship friendship = new Friendship();
        if(friendships.isEmpty()){
            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setName("Request");
            friendshipStatus.setCode(FriendshipStatusCode.REQUEST);

            friendship.setSrcPerson(currentPerson);
            friendship.setDstPerson(dstPerson);
            friendship.setStatus(friendshipStatus);
        }else{
            friendship = friendships.get(0);
            friendship.getStatus().setTime(LocalDateTime.now());
            friendship.getStatus().setName("Friend");
            friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        }

        friendshipRepository.save(friendship);

        return defaultRS;
    }

    public DefaultRS getRequests(String name, Integer offset, Integer itemPerPage) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());
        List<Friendship> friendships = friendshipRepository.findAllRequest(currentPerson);
        List<UserDTO> friends;
        Stream<Person> personStream = friendships.stream()
                .map(Friendship::getSrcPerson);

        if(name.equals("")){
            friends = personStream.map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
        }else{
            friends = personStream.filter(p -> p.getFirstName().equals(name)).map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
        }

        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setData(friends);

        return defaultRS;
    }

    public DefaultRS getRecommendations(Integer offset, Integer itemPerPage) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());

        //лютая заплатка, переписать на sql запрос
        List<UserDTO> recommendations =
                friendshipRepository.findAllFriends(currentPerson).stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipStatusCode.FRIEND))
                .map(f -> f.getSrcPerson().equals(currentPerson) ? f.getDstPerson() : f.getSrcPerson())
                .map(p -> friendshipRepository.findAllFriends(p).stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipStatusCode.FRIEND))
                .map(f -> f.getSrcPerson().equals(p) ? f.getDstPerson() : f.getSrcPerson()).collect(Collectors.toList()))
                .flatMap(l -> l.stream()).filter(p -> !p.equals(currentPerson)).map(p -> personModelMapper.mapToUserDTO(p)).collect(Collectors.toList());
        /////

        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setData(recommendations);

        return defaultRS;
    }

    public List<StatusUserDTO> isFriends(List<Integer> user_ids) {
        UserDTO user = (UserDTO)userService.getUser().getData();
        Person currentPerson = personRepository.getPersonById(user.getId());

        List<Integer> friends= user_ids.stream().map(i -> personRepository.getPersonById(i))
                .filter(p -> isFriend(currentPerson,p))
                .map(p -> p.getId())
                .collect(Collectors.toList());

        List<StatusUserDTO> statusUserDTOS = new ArrayList<>();
                friends.stream()
                .forEach(i ->{StatusUserDTO status = new StatusUserDTO();
                            status.setUserId(i);
                            statusUserDTOS.add(status);
                });

        return statusUserDTOS;
    }

    private boolean isFriend(Person currentPerson, Person dstPerson){
         List<Friendship> friendships = friendshipRepository.isFriends(currentPerson, dstPerson).stream()
                 .filter(f -> f.getStatus().getCode().equals(FriendshipStatusCode.FRIEND)).collect(Collectors.toList());

         return friendships.isEmpty() ? false : true;
    }

}
