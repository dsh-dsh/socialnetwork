package com.skillbox.socialnet.model.RQ;

//import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchRQ {

    //  TODO мапятся только поля с совпадающими именами, @JsonProperty не работает

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("age_from")
    private int ageFrom;

    @JsonProperty("age_to")
    private int ageTo;

    private String country;
    private String city;

    public void firstNameToLower(){
        if (firstName != null) {
            firstName = firstName.toLowerCase();
        }
    }
    public void lastNameToLower(){
        if (lastName != null) {
            lastName = lastName.toLowerCase();
        }
    }
}
