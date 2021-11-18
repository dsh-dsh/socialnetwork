package com.skillbox.socialnet.model.RS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRS {
    private String error;
    private long timestamp;
    private int total;
    private int offset;
    private int perPage;
    private NotificationDataRS data;
}
