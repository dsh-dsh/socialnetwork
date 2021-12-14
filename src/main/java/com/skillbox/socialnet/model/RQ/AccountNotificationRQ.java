package com.skillbox.socialnet.model.RQ;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountNotificationRQ {

    @JsonProperty("notification_type")
    private String notificationType;
//    private NotificationTypeCode notificationType; // TODO Заменил на Стринг, не маппится.
    private boolean enable;
}
