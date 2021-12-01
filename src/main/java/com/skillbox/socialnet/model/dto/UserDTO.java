package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private int id;
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
    private String photo;
    private String about;
    private String city;
    private String country;
    @JsonProperty("messages_permission")
    private MessagesPermission permission;
    @JsonProperty("last_online_time")
    private long lastOnlineTime;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private String token;

}
