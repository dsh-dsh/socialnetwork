package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.exception.NoAnyPostsFoundException;
import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PostModelMapper;
import com.skillbox.socialnet.repository.CommentRepository;
import com.skillbox.socialnet.repository.LikesRepository;
import com.skillbox.socialnet.repository.PostRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final PostModelMapper postModelMapper;
    private final FriendsService friendsService;

    public DefaultRS<?> getPostsByText(String text, long dateFrom, long dateTo, Pageable pageable) {
        dateTo = dateTo == 0 ? new Date().getTime() : dateTo;
        Page<Post> postPage = postRepository.findPostBySearchRequest(text, new Timestamp(dateFrom), new Timestamp(dateTo), pageable);
        List<PostDTO> postsDTOList = postPage.stream()
                .map(postModelMapper::mapToPostDTO).collect(Collectors.toList());
        return DefaultRSMapper.of(postsDTOList, postPage);
    }

    public DefaultRS<?> getFeeds(String name, Pageable pageable) {
        List<PostDTO> postDTOs;
        Page<Post> postPage = postRepository.getOptionalPageAll(pageable)
                .orElseThrow(BadRequestException::new); // TODO заглушка, получать по друзьям
        postDTOs = postPage.getContent().stream()
                .map(postModelMapper::mapToPostDTO)
                .collect(Collectors.toList());
        List<PostDTO> postsDTOList = addFakeComments(postDTOs); // TODO Заглушшка Если comments: null пост не отображается фронтом
        return DefaultRSMapper.of(postDTOs, postPage);
    }

    private List<PostDTO> addFakeComments(List<PostDTO> postDTOList) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setCommentText("comment");
        commentDTO.setAuthorId(3);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(commentDTO);

        List<PostDTO> posts = new ArrayList<>();
        for(PostDTO postDTO : postDTOList) {
            postDTO.setComments(commentDTOList);
            posts.add(postDTO);
        }
        return posts;
    }

    public DefaultRS<?> getPostById(int id) {
        DefaultRS defaultRS = new DefaultRS();
        Optional<Post> optionalPost = postRepository.findPostById(id);
        if (optionalPost.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            defaultRS.setData(getPostDTO(optionalPost.get()));
            }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;
    }

    public DefaultRS<?> changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {

        DefaultRS<PostDTO> defaultRS = new DefaultRS<>();
        Optional<Post> optionalPost = postRepository.findPostById(id);
        if (optionalPost.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            Post post = optionalPost.get();
            if (publishDate != 0) {
                post.setTime(new Timestamp(publishDate));
            }
            if(!postChangeRQ.getPostText().isEmpty()) {
                post.setPostText(postChangeRQ.getPostText());
            }
            if(!postChangeRQ.getTitle().isEmpty()) {
                post.setTitle(postChangeRQ.getTitle());
            }
            postRepository.save(post);
            defaultRS.setData(getPostDTO(post));

        }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;



    }



    public DefaultRS deletePostById(int id) {

        DefaultRS defaultRS = new DefaultRS();
        Optional<Post> optionalPost = postRepository.findPostById(id); // TODO .orElseThrow(noSuchPostException)
        if (optionalPost.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            Post post = optionalPost.get();

            //postRepository.delete(post); // TODO сначала удалять все что ссылается на этот post

            defaultRS.setData(getPostDTO(optionalPost.get()));
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(id);
            defaultRS.setData(locationDTO);
        }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;
    }

    public DefaultRS<?> recoverPostById(int id) {
        return DefaultRSMapper.of(new PostDTO());
    }

    public DefaultRS<?> getCommentsToPost(int id, Pageable pageable) {
        List<CommentDTO> comments = getCommentDTOList(commentRepository.findByPostId(pageable, id));
        return DefaultRSMapper.of(comments);
    }


    public DefaultRS makeCommentToPost(int id, CommentRQ commentRQ) {
        DefaultRS defaultRS = new DefaultRS();
        Optional<Post> optionalPost = postRepository.findPostById(id);
        if (optionalPost.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            Post post = optionalPost.get();
            PostComment postComment = new PostComment();
            postComment.setCommentText(commentRQ.getCommentText());
            if (commentRQ.getParentId() != null ){
                Optional<PostComment> optionalParentComment = commentRepository.findById(commentRQ.getParentId());
                if (optionalParentComment.isPresent()) {
                    postComment.setParent(optionalParentComment.get());
                }
            }
            postComment.setPost(post);
            postComment.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

//            ДОПИСАТЬ АВТОРА КОГДА БУДЕТ АВТОРИЗАЦИЯ ЧЕРЕЗ СЕКЬЮРИТИ
            commentRepository.save(postComment);
            defaultRS.setData(getCommentDTO(postComment));
        }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;
    }

    public DefaultRS rewriteCommentToThePost(int id, int commentId, CommentRQ commentRQ) {

        DefaultRS defaultRS = new DefaultRS();
        Optional<PostComment> optionalPostComment = commentRepository.findById(commentId);
        if (optionalPostComment.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            PostComment postComment = optionalPostComment.get();
            postComment.setCommentText(commentRQ.getCommentText());
            commentRepository.save(postComment);
            defaultRS.setData(getCommentDTO(postComment));
        }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;
    }


    public DefaultRS deleteCommentToThePost(int id, int commentId) {

        DefaultRS defaultRS = new DefaultRS();
        Optional<PostComment> optionalPostComment = commentRepository.findById(commentId);
        if (optionalPostComment.isPresent()) {
            defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
            PostComment postComment = optionalPostComment.get();
            commentRepository.delete(postComment);
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(commentId);
            defaultRS.setData(locationDTO);
        }
        else {
            defaultRS.setError("bad request");
        }
        return defaultRS;

    }

    public DefaultRS<?> recoverCommentToPost(int id, int commentId) {
        return DefaultRSMapper.of(new CommentDTO());
    }

    public DefaultRS<?> reportPostById(int id) {
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> reportCommentToThePost(int id, int commentId) {
        return DefaultRSMapper.of(new MessageDTO());
    }

    private List<CommentDTO> getCommentDTOList(List<PostComment> postCommentList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        postCommentList.forEach(postComment -> {
            commentDTOList.add(getCommentDTO(postComment));
        });
        return commentDTOList;
    }


    private CommentDTO getCommentDTO(PostComment postComment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(postComment.getId());
        commentDTO.setCommentText(postComment.getCommentText());
        commentDTO.setBlocked(postComment.isBlocked());
        commentDTO.setTime(postComment.getTime().getTime());
        commentDTO.setPostId(String.valueOf(postComment.getPost().getId()));
        commentDTO.setAuthorId(postComment.getAuthor().getId());
        if (postComment.getParent() != null){
            commentDTO.setParentId(postComment.getParent().getId());
        }
        return commentDTO;
    }

//    private List<PostDTO> getPostsDTOList(List<Post> postList) {
//        List<PostDTO> postDTOList = new ArrayList<>();
//        postList.forEach(post -> {
//            PostDTO postDTO = getPostDTO(post);
//            postDTOList.add(postDTO);
//
//        });
//        return postDTOList;
//    }

    private PostDTO getPostDTO(Post post)
    {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setPostText(post.getPostText());
        postDTO.setTitle(post.getTitle());
        postDTO.setTime(post.getTime().getTime());
        postDTO.setBlocked(post.isBlocked());
        postDTO.setAuthor(getAuthor(post.getAuthor()));

        postDTO.setComments(getCommentDTOList(commentRepository.findByPost(post)));
        postDTO.setLikes(likesRepository.findByPost(post).size());
        return postDTO;
    }

    private UserDTO getAuthor(Person person) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(person.getId());
        userDTO.setFirstName(person.getFirstName());
        userDTO.setLastName(person.getLastName());
        userDTO.setBlocked(person.isBlocked());
        userDTO.setAbout(person.getAbout());
        userDTO.setPhoto(person.getPhoto());
        userDTO.setBirthDate(person.getBirthDate().getTime());
        userDTO.setLastOnlineTime(person.getLastOnlineTime().getTime());
        userDTO.setPermission(person.getMessagesPermission());
        userDTO.setRegistrationDate(person.getRegData().getTime());
        userDTO.setEmail(person.getEMail());
        userDTO.setPhone(person.getPhone());
//        userDTO.setCity(person.getCity());
//        userDTO.setCountry(person.getCountry());
        return userDTO;
    }

}
