package com.skillbox.socialnet.model.rs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class StatisticsResponse {
    private int posts;

    private int likes;

    private int comments;

    @JsonProperty("first_publication")
    private String firstPublication;

    public StatisticsResponse(Integer sumOfPostsById, Integer sumOfLikes, Integer comments, String firstPublication) {
        this.posts = sumOfPostsById;
        this.likes = sumOfLikes;
        this.comments = comments;
        this.firstPublication = firstPublication;
    }
}
