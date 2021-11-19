package com.skillbox.socialnet.service;
import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
public class AccountService {

    public DefaultRS register() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS recoveryPassword() {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS setPassword(AccountPasswordSetRQ accountPasswordSetRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS setEmail(AccountEmailRQ acctEmailRequest) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }

    public DefaultRS setNotifications(AccountNotificationRQ accountNotificationRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }
}
