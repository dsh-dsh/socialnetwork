package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.NotificationRQ;
import com.skillbox.socialnet.model.RS.NotificationRS;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public NotificationRS getNotification(NotificationRQ notificationRQ){
        return new NotificationRS();
    }
}
