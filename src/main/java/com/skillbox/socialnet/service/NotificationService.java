package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RS.NotificationDataRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PersonRepository personRepository;
    private final AuthService authService;

    public NotificationRS getNotification(int itemPerPage, int offset){
        NotificationRS notificationRS = new NotificationRS();
        notificationRS.setError("error");
        notificationRS.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationRS.setOffset(offset);
        notificationRS.setPerPage(itemPerPage);
        notificationRS.setTotal(0);
        notificationRS.setData(new NotificationDataRS());
        return new NotificationRS();
    }

    public NotificationRS putNotification(boolean all, int id){
        return new NotificationRS();
    }

    public NotificationDataRS createNotificationDataRS(){
        int id = authService.getPersonFromSecurityContext().getId();
        CommentAuthorDTO entityAuthor = personRepository.getPersonForNotification(id);
        return new NotificationDataRS();
    }

}
