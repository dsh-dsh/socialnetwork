package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.NotificationRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.NotificationDataRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class NotificationService {

    public NotificationRS getNotification(NotificationRQ notificationRQ){
        return new NotificationRS();
    }

    public NotificationRS setNotification(NotificationRQ notificationRQ){
        return new NotificationRS();
    }

}
