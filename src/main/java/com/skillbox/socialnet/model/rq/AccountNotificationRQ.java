package com.skillbox.socialnet.model.rq;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.validation.annotation.ValidNotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountNotificationRQ {

    @ValidNotificationType()
    @JsonProperty("notification_type")
    private String notificationType;

    private boolean enable;
}
