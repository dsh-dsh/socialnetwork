package com.skillbox.socialnet.model.rs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Data
public class StatisticsResponse {
    private int posts;

    private int likes;

    private int comments;

    @JsonProperty("first_publication")
    private Timestamp firstPublication;

    public StatisticsResponse(Integer sumOfPostsById, Integer sumOfLikes, Integer comments, Timestamp firstPublication) {
        this.posts = sumOfPostsById;
        this.likes = sumOfLikes;
        this.comments = comments;
        this.firstPublication = firstPublication;
    }
}