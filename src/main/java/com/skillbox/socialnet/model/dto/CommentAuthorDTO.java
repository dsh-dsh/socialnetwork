package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Person;
import lombok.Data;

@Data
public class CommentAuthorDTO {
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String photo;

    public static CommentAuthorDTO getCommentAuthorDTO(Person person){
        CommentAuthorDTO commentAuthorDTO = new CommentAuthorDTO();
        commentAuthorDTO.setId(person.getId());
        commentAuthorDTO.setFirstName(person.getFirstName());
        commentAuthorDTO.setLastName(person.getLastName());
        commentAuthorDTO.setPhoto(person.getPhoto());
        return commentAuthorDTO;
    }
}
