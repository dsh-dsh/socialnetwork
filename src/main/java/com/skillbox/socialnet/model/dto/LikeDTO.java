package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.socialnet.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDTO {
    String likes = "string";
    List<String> users = new ArrayList<>();

    public static void addUser(LikeDTO likeDTO, Person person) {
        likeDTO.users.add(person.getFirstName() + " " + person.getLastName());
    }
}

