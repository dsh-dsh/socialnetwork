package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RQ.AccountRegisterRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.skillbox.socialnet.config.Config.bcrypt;


/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PersonRepository personRepository;

    public DefaultRS<?> register(AccountRegisterRQ accountRegisterRQ) {
        if (!isEmailExist(accountRegisterRQ.getEmail())) {
            Person person = new Person();
            person.setEMail(accountRegisterRQ.getEmail());
            person.setFirstName(accountRegisterRQ.getFirstName());
            person.setLastName(accountRegisterRQ.getLastName());
            person.setPassword(bcrypt(accountRegisterRQ.getPasswd1()));
            personRepository.save(person);
            return DefaultRSMapper.of(new MessageDTO());
        }
//        defaultRS.setErrorDesc("Email is already in use."); // TODO добавить в DefaultRSMapper.error .setErrorDesc
        return DefaultRSMapper.error("User already exist!");
    }

    private boolean isEmailExist(String email) {
        Person person = personRepository.findByeMail(email);
        return (person != null)? true : false;
    }

    public DefaultRS<?> recoveryPassword() {
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> setPassword(AccountPasswordSetRQ accountPasswordSetRQ) {
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> setEmail(AccountEmailRQ acctEmailRequest) {
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> setNotifications(AccountNotificationRQ accountNotificationRQ) {
        return DefaultRSMapper.of(new MessageDTO());
    }


}
