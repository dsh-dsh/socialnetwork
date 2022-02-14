package com.skillbox.socialnet.model.rs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRS {
    private List<NotificationDataRS> data;
    private String error;
    private int offset;
    private int perPage;
    private Timestamp timestamp;
    private int total;
}
