package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.dto.DeleteLikeDTO;
import com.skillbox.socialnet.model.dto.LikeDTO;
import com.skillbox.socialnet.model.dto.LikedDTO;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostLike;
import com.skillbox.socialnet.repository.LikesRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final PersonRepository personRepository;

    public LikeDTO getLikes(int postId) {
        LikeDTO likes = new LikeDTO();
        List<PostLike> likeList = likesRepository.findAllByPost(postRepository.findById(postId));
        for (PostLike l :
                likeList) {
            LikeDTO.addUser(likes, l.getPerson());
        }
        return likes;
    }

    public LikeDTO setLike(int postId) {
        PostLike postLike = new PostLike();
        postLike.setPost(postRepository.findPostById(postId).orElseThrow(BadRequestException::new));
        postLike.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        postLike.setPerson(authService.getPersonFromSecurityContext());
        likesRepository.save(postLike);
        LikeDTO likeDTO = new LikeDTO();
        LikeDTO.addUser(likeDTO, postLike.getPerson());
        return likeDTO;
    }

    public LikedDTO getLiked(int postId, int userId) {
        Post post = postRepository.findById(postId);
        if (post == null){
            throw new BadRequestException();
        }
        Optional<PostLike> postLike =
                likesRepository.findByPostAndPerson(post, personRepository.findPersonById(userId));
        boolean prop = postLike.isPresent();
        return new LikedDTO(prop, prop, prop);
    }

    public DeleteLikeDTO deleteLike(int postId){
        PostLike postLike =
                likesRepository.findByPostAndPerson(postRepository.findById(postId), authService.getPersonFromSecurityContext()).orElseThrow(BadRequestException::new);
        likesRepository.delete(postLike);
        return new DeleteLikeDTO();
    }

}
