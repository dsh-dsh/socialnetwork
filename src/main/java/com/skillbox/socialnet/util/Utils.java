package com.skillbox.socialnet.util;

import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.UserDTO;

import java.util.Calendar;
import java.util.List;

public class Utils {

    public static DefaultRS<UserDTO> defaultRS(UserDTO data) {
        DefaultRS<UserDTO> defaultRS = new DefaultRS();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static DefaultRS<List<UserDTO>> defaultRS(List<UserDTO> data) {
        DefaultRS<List<UserDTO>> defaultRS = new DefaultRS();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(data);
        return defaultRS;
    }
}
