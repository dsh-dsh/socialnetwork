package com.skillbox.socialnet.model.RQ;

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
    @Email
    String email;
}
