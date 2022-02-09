package com.skillbox.socialnet.model.RQ;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.annotation.IsEmailExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * @author Semen V
 * @created 19|11|2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEmailRQ {

    @Email(message = Constants.NOT_VALID_EMAIL_MESSAGE)
    @IsEmailExists
    private String email;
}
