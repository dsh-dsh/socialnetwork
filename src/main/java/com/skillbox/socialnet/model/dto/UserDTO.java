package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.MessagePermissions;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private long registrationDate;
    @JsonProperty("birth_date")
    private long birthDate;
    private String email;
    private String phone;
    private String about;
    private LocationDTO city;
    private LocationDTO country;
    @JsonProperty("messages_permission")
    private MessagePermissions permission;
    @JsonProperty("last_online_time")
    private long lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private String token;

}
