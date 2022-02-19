package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.PostComment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private int id;
    private long time;

    @JsonProperty("post_id")
    private String postId;

    @JsonProperty("parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int parentId;

    @JsonProperty("comment_text")
    private String commentText;

    private CommentAuthorDTO author;

    private boolean blocked;

    private int likes;

    @JsonProperty("my_like")
    private int myLike;

    @JsonProperty("sub_comments")
    private List<CommentDTO> subComments = new ArrayList<>();


    public static CommentDTO getCommentDTO(PostComment postComment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(postComment.getId());
        commentDTO.setTime(postComment.getTime().getTime());
        commentDTO.setPostId(String.valueOf(postComment.getPost().getId()));
        if (postComment.getParent() != null) {
            commentDTO.setParentId(postComment.getParent().getId());
        }
        commentDTO.setCommentText(postComment.getCommentText());
        commentDTO.setAuthor(CommentAuthorDTO.getCommentAuthorDTO(postComment.getAuthor()));
        commentDTO.setBlocked(postComment.isBlocked());
        return commentDTO;
    }
}
