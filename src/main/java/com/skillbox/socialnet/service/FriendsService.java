package com.skillbox.socialnet.service;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.StatusUserDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import liquibase.pro.packaged.L;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
public class FriendsService {

    //заглушка
    private static UserDTO userDTO;
    static{
        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setFirstName("Семён");
        userDTO.setLastName("Семёныч");
        userDTO.setRegistrationDate(Calendar.getInstance().getTimeInMillis());
        userDTO.setBirthDate(Calendar.getInstance().getTimeInMillis());
        userDTO.setEmail("petr@mail.ru");
        userDTO.setPhone( "891111111111");
        userDTO.setAbout("Родился в небольшой, но честной семье");
        userDTO.setCity(new LocationDTO());
        userDTO.setCountry(new LocationDTO());
        userDTO.setPermission(MessagesPermission.ALL);
        userDTO.setLastOnlineTime(Calendar.getInstance().getTimeInMillis());
        userDTO.setBlocked(false);
        userDTO.setPhoto("https://vsthemes.org/uploads/posts/2019-03/1195118549.jpg");

    }


    public DefaultRS getAllFriends(String name, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);

        //List<UserDTO> listFriends = getFriends(name)

        List<UserDTO> listFriends = new ArrayList<>();
        listFriends.add(userDTO);

        defaultRS.setData(listFriends);
        return defaultRS;
    }

    public DefaultRS deleteFriend(int id) {
        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS addFriend(int id) {
        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS getRequests(String name, Integer offset, Integer itemPerPage) {
        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);

        //List<UserDTO> listFriends = getFriends(name)

        List<UserDTO> listFriends = new ArrayList<>();
        listFriends.add(userDTO);

        defaultRS.setData(listFriends);
        return defaultRS;
    }

    public DefaultRS getRecommendations(Integer offset, Integer itemPerPage) {
        DefaultRS defaultRS = new DefaultRS<List<UserDTO>>();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);

        //List<UserDTO> listFriends = getFriends(name)

        List<UserDTO> listFriends = new ArrayList<>();
        listFriends.add(userDTO);

        defaultRS.setData(listFriends);
        return defaultRS;
    }

    public List<StatusUserDTO> isFriends(List<Integer> user_ids) {
        //List<StatusUserDTO> statusUserList  = getStatus(user_ids)

        List<StatusUserDTO> statusUserDTOS = new ArrayList<>();
        StatusUserDTO userStatus = new StatusUserDTO();
        userStatus.setUserId(3);
        userStatus.setStatus("FRIEND");
        statusUserDTOS.add(userStatus);

        return statusUserDTOS;
    }




}
