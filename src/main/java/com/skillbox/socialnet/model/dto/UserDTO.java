package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonProperty("is_friend")
    private boolean isFriend;
    @JsonProperty("is_you_blocked")
    private boolean isYouBlocked;
    private boolean me;
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    private String token;

    public static UserDTO getUserDTO(Person person){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(person.getId());
        userDTO.setFirstName(person.getFirstName());
        userDTO.setLastName(person.getLastName());
        userDTO.setRegistrationDate(person.getRegDate().getTime());
        userDTO.setBirthDate((person.getBirthDate() == null)? 0 : person.getBirthDate().getTime());
        userDTO.setEmail(person.getEMail());
        userDTO.setPhone(person.getPhone());
        userDTO.setPhoto(person.getPhoto());
        userDTO.setAbout(person.getAbout());
        userDTO.setCity(person.getCity());
        userDTO.setCountry(person.getCountry());
        userDTO.setPermission(person.getMessagesPermission());
        userDTO.setLastOnlineTime(person.getLastOnlineTime().getTime());
        userDTO.setBlocked(person.isBlocked());
        userDTO.setDeleted(person.isDeleted());

        return userDTO;
    }
}


