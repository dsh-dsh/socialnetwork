package com.skillbox.socialnet.model.dto;

import com.skillbox.socialnet.model.entity.NotificationSetting;
import lombok.Data;

@Data
public class NotificationSettingsDto {
    private boolean enable;
    private String type;

    public NotificationSettingsDto(NotificationSetting notificationSetting) {
        this.enable = notificationSetting.isPermission();
        this.type = String.valueOf(notificationSetting.getNotificationTypeCode());
    }
}
