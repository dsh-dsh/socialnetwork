package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.RS.DefaultRS;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AuthService {


    public DefaultRS login() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getUserDTO());
        return defaultRS;
    }

    public DefaultRS logout() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    private UserDTO getUserDTO() {
        return new UserDTO();
    }


}
