package com.skillbox.socialnet.service;

import com.skillbox.socialnet.api.response.DefaultResponse;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AuthService {


    public DefaultResponse login() {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getUserDTO());
        return defaultResponse;
    }

    public DefaultResponse logout() {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(new MessageDTO());
        return defaultResponse;
    }

    private UserDTO getUserDTO() {
        return new UserDTO();
    }


}
