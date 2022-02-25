package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.rs.StatisticsResponse;
import com.skillbox.socialnet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final UserService userService;
    private final PostRepository postRepository;


    public StatisticsResponse getStatisticsResponse(){
            int id = userService.getUser().getId();
            return new StatisticsResponse(
                    postRepository.getSumOfPostsById(id) == null ? 0 : postRepository.getSumOfPostsById(id),
                    postRepository.getSumOfLikes(id) == null ? 0 : postRepository.getSumOfLikes(id),
                    postRepository.getSumOfComments(id) == null ? 0 : postRepository.getSumOfComments(id),
                    postRepository.getFirstPublication(id) == null ? "Нет постов" : postRepository.getFirstPublication(id).toString()
            );
        }
}
