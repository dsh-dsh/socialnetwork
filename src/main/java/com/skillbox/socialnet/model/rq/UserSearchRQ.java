package com.skillbox.socialnet.model.rq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchRQ {

    private String firstName;
    private String lastName;
    private int ageFrom;
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
