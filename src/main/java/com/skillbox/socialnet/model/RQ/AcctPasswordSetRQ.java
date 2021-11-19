package com.skillbox.socialnet.model.RQ;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Semen V
 * @created 18|11|2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcctPasswordSetRQ {

    private String token;
    private String password;

}
