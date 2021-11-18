package com.skillbox.socialnet.model.RQ;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AcctNotificationRQ {

    @JsonProperty("notification_type")
    private RequestMethod notificationType;
    private boolean enable;
}
