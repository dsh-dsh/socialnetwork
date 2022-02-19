package com.skillbox.socialnet.model.rq;


import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.annotation.CodeExpiration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPasswordSetRQ {

    @CodeExpiration
    private String token;

    @Size(min = 8,
            message = Constants.PASSWORD_NOT_VALID_MESSAGE)
    private String password;

}
