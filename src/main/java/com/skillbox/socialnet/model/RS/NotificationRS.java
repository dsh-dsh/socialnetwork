package com.skillbox.socialnet.model.RS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRS {
    private NotificationDataRS data;
    private String error;
    private int offset;
    private int perPage;
    private Timestamp timestamp;
    private int total;
}
