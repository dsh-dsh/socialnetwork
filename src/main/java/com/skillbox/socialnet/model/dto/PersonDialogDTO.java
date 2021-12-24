package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonDialogDTO {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String photo;
    @JsonProperty("e_mail")
    private String eMail;
    @JsonProperty("last_online_time")
    private long lastOnlineTime;
}
