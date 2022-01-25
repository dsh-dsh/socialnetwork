package com.skillbox.socialnet.model.RQ;


import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.anotation.CodeExpiration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * @author Semen V
 * @created 18|11|2021
 */

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
