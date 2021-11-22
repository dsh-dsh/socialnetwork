package com.skillbox.socialnet.service;
import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RQ.AccountRegisterRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.entity.User;
import org.springframework.stereotype.Service;
import org.thymeleaf.IThrottledTemplateProcessor;

import java.util.Calendar;
import java.util.Optional;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
public class AccountService {

//    public DefaultRS register(AccountRegisterRQ accountRegisterRQ) {
//        DefaultRS defaultRS = new DefaultRS();
//        if (!isEmailExist(accountRegisterRQ.getEmail())){
//            User user = new User();
//            user.setEMail(accountRegisterRQ.getEmail());
//            user.setName(accountRegisterRQ.getFirstName() + " " + accountRegisterRQ.getLastName());
//            user.setPassword(accountRegisterRQ.getPasswd1());
//            userRepository.save(user);
//            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
//            defaultRS.setData(new MessageDTO());
//            return defaultRS;
//        }
//        defaultRS.setError("User already exist!");
//        defaultRS.setErrorDesc("Email is already in use.");
//        return defaultRS;
//    }

    //   private boolean isEmailExist(String email){
    //    Optional<User> users = userRepository.findByEmail(email);
    //   return users.isPresent();
   // }

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
