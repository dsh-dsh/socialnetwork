package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeRQ {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private long birthDate;
    private String phone;
    @JsonProperty("photo_id")
    private String photoId;
    private String about;
    @JsonProperty("town_id")
    private int townId;
    @JsonProperty("country_id")
    private int countryId;
    @JsonProperty("message_permission")
    private MessagesPermission messagesPermission;
}
