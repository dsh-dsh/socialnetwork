package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.LikeDTO;
import com.skillbox.socialnet.model.entity.PostLike;
import com.skillbox.socialnet.repository.LikesRepository;
import com.skillbox.socialnet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public LikeDTO getLikes(int postId) {
        LikeDTO likes = new LikeDTO();
        List<PostLike> likeList = likesRepository.findAllByPost(postRepository.findById(postId));
        for (PostLike l :
                likeList) {
            LikeDTO.addUser(likes, l.getPerson());
        }
        return likes;
    }


}
