package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Person;
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

    public PersonDialogDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.photo = person.getPhoto();
        this.eMail = person.getEMail();
        this.lastOnlineTime = person.getLastOnlineTime().getTime();
    }
}
