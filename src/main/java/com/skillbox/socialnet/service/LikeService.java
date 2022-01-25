package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.NoSuchPostException;
import com.skillbox.socialnet.model.dto.DeleteLikeDTO;
import com.skillbox.socialnet.model.dto.LikeDTO;
import com.skillbox.socialnet.model.dto.LikedDTO;
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
        postLike.setPost(postRepository.findPostById(postId).orElseThrow(NoSuchPostException::new));
//        postLike.setPost(postRepository.findById(postId));
        postLike.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        postLike.setPerson(authService.getPersonFromSecurityContext());
        //todo create notification for like
        likesRepository.save(postLike);
        LikeDTO likeDTO = new LikeDTO();
        LikeDTO.addUser(likeDTO, postLike.getPerson());
        return likeDTO;
    }

    public LikedDTO getLiked(int postId, int userId) {
        Optional<PostLike> postLikeOptional =
                likesRepository.findByPostAndPerson(postRepository.findById(postId), personRepository.findPersonById(userId));
        boolean prop = postLikeOptional.isPresent();
        return new LikedDTO(prop, prop, prop);
    }

    public DeleteLikeDTO deleteLike(int postId){
        Optional<PostLike> postLikeOptional =
                likesRepository.findByPostAndPerson(postRepository.findById(postId), authService.getPersonFromSecurityContext());
        postLikeOptional.ifPresent(likesRepository::delete);
        return new DeleteLikeDTO();
    }

}
