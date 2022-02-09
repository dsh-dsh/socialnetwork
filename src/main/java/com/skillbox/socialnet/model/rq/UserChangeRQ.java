package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeRQ {
    @JsonProperty("first_name")
    @NotBlank(message = Constants.WRONG_FIRST_NAME_MESSAGE)
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = Constants.WRONG_LAST_NAME_MESSAGE)
    private String lastName;

    @JsonProperty("birth_date")
    @Past(message = Constants.NOT_VALID_BIRTHDAY_MESSAGE)
    private Timestamp birthDate;

    @PhoneNumber
    private String phone;

    private String about;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("message_permission")
    private MessagesPermission messagesPermission;
}
