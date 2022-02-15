package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.skillbox.socialnet.util.Constants.STRING;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteLikeDTO {

    String additionalProp1 = STRING;
    String additionalProp2 = STRING;
    String additionalProp3 = STRING;
}
