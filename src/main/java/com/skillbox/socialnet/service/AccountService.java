package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RQ.AccountRegisterRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.entity.NotificationSetting;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.repository.SettingsRepository;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.skillbox.socialnet.config.Config.bcrypt;


/**
 * @author Semen V
 * @created 18|11|2021
 */

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final SettingsRepository settingsRepository;

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
        return DefaultRSMapper.error(Constants.BAD_REQUEST_MESSAGE);
    }

    private boolean isEmailExist(String email) {
        Person person = personRepository.findByeMail(email).orElse(null);
        return (person != null)? true : false;
    }

    public DefaultRS<?> recoveryPassword(
            AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) throws MailException {

        String email = accountEmailRQ.getEmail();
        String recoveryLink = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getServletPath(), "") +
                "/change-password?code=" + getConfirmationCode(email);
        emailService.send(email, Constants.PASSWWORD_RECOVERY_SUBJECT,
                    String.format(Constants.PASSWWORD_RECOVERY_TEXT, recoveryLink));
        return DefaultRSMapper.of(new MessageDTO());
    }

    private String getConfirmationCode(String email) {
        Person person = personRepository.findByeMail(email)
                .orElseThrow(NoSuchUserException::new);
        String confirmationCode = jwtProvider.generateConfirmationCode(person);
        person.setConfirmationCode(confirmationCode);
        personRepository.save(person);
        return confirmationCode;
    }

    public DefaultRS<?> setPassword(AccountPasswordSetRQ accountPasswordSetRQ) {
        if(!jwtProvider.validateConfirmationCode(accountPasswordSetRQ.getToken())){
            throw new BadRequestException();
        }
        Person person = personRepository.findByConfirmationCode(accountPasswordSetRQ.getToken())
                .orElseThrow(BadRequestException::new);
        person.setPassword(passwordEncoder.encode(accountPasswordSetRQ.getPassword()));
        personRepository.save(person);
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> shiftEmail(HttpServletRequest servletRequest) throws MailException{
        String email = authService.getPersonFromSecurityContext().getEMail();
        String recoveryLink = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getServletPath(), "") +
                "/shift-email";
        emailService.send(email, Constants.EMAIL_RECOVERY_SUBJECT,
                String.format(Constants.EMAIL_RECOVERY_TEXT, recoveryLink));
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> setEmail(AccountEmailRQ accountEmailRQ) {
        String email = accountEmailRQ.getEmail();
        if(isEmailExist(email)) {
            throw new BadRequestException();
        }
        Person person = authService.getPersonFromSecurityContext();
        person.setEMail(email);
        personRepository.save(person);
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> setNotifications(AccountNotificationRQ accountNotificationRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        NotificationTypeCode notificationTypeCode = NotificationTypeCode.valueOf(accountNotificationRQ.getNotificationType());

        NotificationSetting notificationSetting = getNotificationSetting(currentPerson, notificationTypeCode);
        notificationSetting.setPermission(!notificationSetting.isPermission());
        settingsRepository.save(notificationSetting);

        return DefaultRSMapper.of(new MessageDTO());
    }

    private NotificationSetting getNotificationSetting(Person person, NotificationTypeCode notificationTypeCode) {
        return settingsRepository
                .findByPersonAndNotificationTypeCode(person, notificationTypeCode)
                .orElse(new NotificationSetting(person, notificationTypeCode, false));
    }
}
