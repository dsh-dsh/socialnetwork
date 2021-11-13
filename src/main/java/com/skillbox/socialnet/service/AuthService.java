package com.skillbox.socialnet.service;

import com.skillbox.socialnet.api.request.AuthUserRequest;
import com.skillbox.socialnet.api.response.BaseResponse;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AuthService {


    public BaseResponse login() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        baseResponse.setData(getUserDTO());
        return baseResponse;
    }

    public BaseResponse logout() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        baseResponse.setData(new MessageDTO());
        return baseResponse;
    }

    private UserDTO getUserDTO() {
        return new UserDTO();
    }


}
