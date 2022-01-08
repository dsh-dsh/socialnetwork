package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.RQ.AccountEmailRQ;
import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RQ.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.RQ.AccountRegisterRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.MessageResponseDTO;
import com.skillbox.socialnet.model.entity.NotificationSetting;
import com.skillbox.socialnet.model.entity.Person;
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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static com.skillbox.socialnet.config.Config.bcrypt;
import static com.skillbox.socialnet.util.Constants.OK;
import static com.skillbox.socialnet.util.Constants.USER_ALREADY_EXIST;


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

   public MessageOkDTO register(AccountRegisterRQ accountRegisterRQ) {
        if (isEmailExist(accountRegisterRQ.getEmail())) {
            throw new BadRequestException(Constants.EMAIL_EXISTS_MESSAGE);
        }
        Person person = new Person();
        person.setEMail(accountRegisterRQ.getEmail());
        person.setFirstName(accountRegisterRQ.getFirstName());
        person.setLastName(accountRegisterRQ.getLastName());
        person.setPassword(bcrypt(accountRegisterRQ.getPasswd1()));
        person.setLastOnlineTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        person.setRegDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        personRepository.save(person);

        return new MessageOkDTO();
    }

    private boolean isEmailExist(String email) {
        Person person = personRepository.findByeMail(email).orElse(null);
        return person != null;
    }

    public MessageOkDTO recoveryPassword(
            AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) throws MailException {
        String email = accountEmailRQ.getEmail();
        String recoveryLink = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getServletPath(), "") +
                "/change-password?code=" + getConfirmationCode(email);
        emailService.send(email, Constants.PASSWWORD_RECOVERY_SUBJECT,
                    String.format(Constants.PASSWWORD_RECOVERY_TEXT, recoveryLink));

        return new MessageOkDTO();
    }

    public String getConfirmationCode(String email) {
        Person person = personRepository.findByeMail(email)
                .orElseThrow(NoSuchUserException::new);
        String confirmationCode = jwtProvider.generateConfirmationCode(person);
        person.setConfirmationCode(confirmationCode);
        personRepository.save(person);

        return confirmationCode;
    }

    public MessageOkDTO setPassword(AccountPasswordSetRQ accountPasswordSetRQ) {
        jwtProvider.validateConfirmationCode(accountPasswordSetRQ.getToken());
        Person person = personRepository.findByConfirmationCode(accountPasswordSetRQ.getToken())
                .orElseThrow(BadRequestException::new);
        person.setPassword(passwordEncoder.encode(accountPasswordSetRQ.getPassword()));
        personRepository.save(person);

        return new MessageOkDTO();
    }

    public MessageOkDTO shiftEmail(HttpServletRequest servletRequest) throws MailException{
        String email = authService.getPersonFromSecurityContext().getEMail();
        String recoveryLink = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getServletPath(), "") +
                "/shift-email";
        emailService.send(email, Constants.EMAIL_RECOVERY_SUBJECT,
                String.format(Constants.EMAIL_RECOVERY_TEXT, recoveryLink));

        return new MessageOkDTO();
    }

    public MessageOkDTO setEmail(AccountEmailRQ accountEmailRQ) {
        String email = accountEmailRQ.getEmail();
        if(isEmailExist(email)) {
            throw new BadRequestException(Constants.EMAIL_EXISTS_MESSAGE);
        }
        Person person = authService.getPersonFromSecurityContext();
        person.setEMail(email);
        personRepository.save(person);

        return new MessageOkDTO();
    }

    public MessageOkDTO setNotifications(AccountNotificationRQ accountNotificationRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        NotificationTypeCode notificationTypeCode = NotificationTypeCode.valueOf(accountNotificationRQ.getNotificationType());
        NotificationSetting notificationSetting = getNotificationSetting(currentPerson, notificationTypeCode);
        notificationSetting.setPermission(!notificationSetting.isPermission());
        settingsRepository.save(notificationSetting);

        return new MessageOkDTO();
    }

    private NotificationSetting getNotificationSetting(Person person, NotificationTypeCode notificationTypeCode) {
        return settingsRepository
                .findByPersonAndNotificationTypeCode(person, notificationTypeCode)
                .orElse(new NotificationSetting(person, notificationTypeCode, false));
    }
}
