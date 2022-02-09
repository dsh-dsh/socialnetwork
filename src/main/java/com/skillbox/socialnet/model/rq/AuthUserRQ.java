package com.skillbox.socialnet.model.rq;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserRQ {

    private String email;
    private String password;

}
