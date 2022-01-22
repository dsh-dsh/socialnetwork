package com.skillbox.socialnet.model.RQ;


import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.anotation.IsEmailExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRQ {

    @Email(message = Constants.NOT_VALID_EMAIL_MESSAGE)
    @IsEmailExists
    private String email;

    @Size(min = 8,
            message = Constants.PASSWORD_TOO_SHORT_MESSAGE)
    private String passwd1;

    private String passwd2;

    @NotBlank(message = Constants.WRONG_FIRST_NAME_MESSAGE)
    private String firstName;

    @NotBlank(message = Constants.WRONG_LAST_NAME_MESSAGE)
    private String lastName;

}
