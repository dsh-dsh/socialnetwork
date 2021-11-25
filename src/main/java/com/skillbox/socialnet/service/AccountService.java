package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RQ.AccountRegisterRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;


/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
public class AccountService {

    private final PersonRepository personRepository;

    public AccountService(PersonRepository userRepository) {
        this.personRepository = userRepository;
    }


    public DefaultRS register(AccountRegisterRQ accountRegisterRQ) {
        DefaultRS defaultRS = new DefaultRS();
        if (!isEmailExist(accountRegisterRQ.getEmail())) {
            Person person = new Person();
            person.setEMail(accountRegisterRQ.getEmail());
            person.setFirstName(accountRegisterRQ.getFirstName());
            person.setLastName(accountRegisterRQ.getLastName());
   //         person.setPassword(bcrypt(accountRegisterRQ.getPasswd1()));
            personRepository.save(person);
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            defaultRS.setData(new MessageDTO());
            return defaultRS;
        }
        defaultRS.setError("User already exist!");
        defaultRS.setErrorDesc("Email is already in use.");
        return defaultRS;
    }

    private boolean isEmailExist(String email) {
        Person person = personRepository.findByeMail(email);
        return (person != null)? true : false;
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
