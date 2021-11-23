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
    public DefaultRS getNotification(NotificationRQ notificationRQ){
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(notificationRQ.getOffset());
        defaultRS.setPerPage(notificationRQ.getItemPerPage());
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setTotal(0);
        List<NotificationDataRS> notificationRSList = new ArrayList<>();
        notificationRSList.add(new NotificationDataRS());
        defaultRS.setData(notificationRSList);
        return defaultRS;
    }
}
