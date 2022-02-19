package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.exception.NoSuchUserException;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.NotificationSettingsDto;
import com.skillbox.socialnet.model.entity.NotificationSetting;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.model.rq.AccountEmailRQ;
import com.skillbox.socialnet.model.rq.AccountNotificationRQ;
import com.skillbox.socialnet.model.rq.AccountPasswordSetRQ;
import com.skillbox.socialnet.model.rq.AccountRegisterRQ;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.repository.SettingsRepository;
import com.skillbox.socialnet.security.JwtProvider;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final SettingsRepository settingsRepository;

    @Value("${expired.confirmation.code.milliseconds}")
    private long expirationTime;


    public MessageOkDTO register(AccountRegisterRQ accountRegisterRQ) {
        Person person = createNewPerson(accountRegisterRQ);
        personRepository.save(person);
        setNotificationSettingsOnRegistering(person);

        return new MessageOkDTO();
    }

    private Person createNewPerson(AccountRegisterRQ accountRegisterRQ) {
        Person person = new Person();
        person.setEMail(accountRegisterRQ.getEmail());
        person.setFirstName(accountRegisterRQ.getFirstName());
        person.setLastName(accountRegisterRQ.getLastName());
        person.setPassword(passwordEncoder.encode(accountRegisterRQ.getPasswd1()));
        person.setLastOnlineTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        person.setRegDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        person.setApproved(true);
        return person;
    }

    private void setNotificationSettingsOnRegistering(Person person) {
        NotificationTypeCode[] typeCodes = NotificationTypeCode.values();
        for (NotificationTypeCode typeCode : typeCodes) {
            NotificationSetting setting = getNotificationSetting(person, typeCode);
            setting.setPermission(true);
            settingsRepository.save(setting);
        }
    }

    private boolean isEmailExist(String email) {
        Person person = personRepository.findByeMail(email).orElse(null);
        return person != null;
    }

    public MessageOkDTO recoveryPassword(
            AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) throws MailException {
        String email = accountEmailRQ.getEmail();
        if(!isEmailExist(email)) {
            throw new BadRequestException(Constants.NO_SUCH_USER_MESSAGE);
        }
        String recoveryLink = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getServletPath(), "") +
                "/change-password?code=" + addConfirmationCode(email);
        emailService.send(email, Constants.PASSWWORD_RECOVERY_SUBJECT,
                    String.format(Constants.PASSWWORD_RECOVERY_TEXT, recoveryLink));

        return new MessageOkDTO();
    }

    public String addConfirmationCode(String email) {
        Person person = personRepository.findByeMail(email)
                .orElseThrow(NoSuchUserException::new);
        String confirmationCode = getConfirmationCodeString();
        person.setConfirmationCode(confirmationCode);
        personRepository.save(person);

        return confirmationCode;
    }

    private String getConfirmationCodeString() {
        long expiration = System.currentTimeMillis() + expirationTime;
        return UUID.randomUUID()
                .toString().replace("-", "") + Constants.EXPIRATION_PREFIX + expiration;
    }

    public MessageOkDTO setPassword(AccountPasswordSetRQ accountPasswordSetRQ) {
        Person person = personRepository.findByConfirmationCode(accountPasswordSetRQ.getToken())
                .orElseThrow(() -> new BadRequestException(Constants.WRONG_RECOVERING_CODE));
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
        Person person = authService.getPersonFromSecurityContext();
        person.setEMail(accountEmailRQ.getEmail());
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

    public List<NotificationSettingsDto> getNotifications() {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<NotificationSetting> settings = settingsRepository.findByPerson(currentPerson);

        return settings.stream().map(NotificationSettingsDto::new)
                .collect(Collectors.toList());
    }
}
