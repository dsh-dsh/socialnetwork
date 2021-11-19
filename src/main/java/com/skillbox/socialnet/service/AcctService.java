package com.skillbox.socialnet.service;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
public class AcctService {

    public DefaultRS register() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS passwordRecovery() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }


}
