package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PostCommentMapper;
import com.skillbox.socialnet.model.mapper.PostMapper;
import com.skillbox.socialnet.repository.CommentRepository;
import com.skillbox.socialnet.repository.LikesRepository;
import com.skillbox.socialnet.repository.PostRepository;
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
    private final PostMapper postMapper;
    private final FriendsService friendsService;
    private final AuthService authService;
    private final PostCommentMapper commentMapper;

//    public DefaultRS<?> searchPosts(String author, String text, long dateFrom, long dateTo, List<String> tags, Pageable pageable) {
//        dateTo = checkDate(dateTo);
//        Page<Post> postPage = postRepository.findPost(
//                author,
//                text,
//                new Timestamp(dateFrom),
//                new Timestamp(dateTo),
//                tags,
//                pageable);
//        List<PostDTO> postsDTOList = postPage.stream()
//                .map(postMapper::mapToPostDTO)
//                .collect(Collectors.toList());
//        return DefaultRSMapper.of(postsDTOList, postPage);
//    }

    public DefaultRS<?> searchPosts(PostSearchRQ postSearchRQ, Pageable pageable) {
        long dateTo = checkDate(postSearchRQ.getDate_to());

        // FIXME если тегов нет фронт посылает пустой массив, найти решение проверки :tags is empty в hql
        if(postSearchRQ.getTags() != null) {
            if (postSearchRQ.getTags().size() == 0) {
                postSearchRQ.setTags(null);
            }
        }

        Page<Post> postPage = postRepository.findPost(
                postSearchRQ.getAuthor(), postSearchRQ.getText(),
                new Timestamp(postSearchRQ.getDate_from()), new Timestamp(dateTo),
                postSearchRQ.getTags(), pageable);
        List<PostDTO> postsDTOList = postPage.stream()
                .map(postMapper::mapToPostDTO)
                .collect(Collectors.toList());
        return DefaultRSMapper.of(postsDTOList, postPage);
    }

    public DefaultRS<?> getFeeds(String name, Pageable pageable) {
        List<Person> friends = friendsService.getMyFriends();
        Page<Post> postPage = postRepository.findByAuthorIn(friends, pageable)
                .orElseThrow(BadRequestException::new);
        List<PostDTO> postDTOs = postPage.getContent().stream()
                .map(postMapper::mapToPostDTO)
                .collect(Collectors.toList());
        return DefaultRSMapper.of(postDTOs, postPage);
    }

    public DefaultRS<?> getPostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);
        PostDTO postDTO = postMapper.mapToPostDTO(post);
        return DefaultRSMapper.of(postDTO);
    }

    public DefaultRS<?> changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);
        changePostPublishDate(publishDate, post);
        changePostTexts(postChangeRQ, post);
        postRepository.save(post);
        PostDTO postDTO = postMapper.mapToPostDTO(post);
        return DefaultRSMapper.of(postDTO);
    }


    public DefaultRS<?> deletePostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);
//            postRepository.delete(post); // TODO сначала удалять все что ссылается на этот post
        DeleteDTO deleteDTO = new DeleteDTO(id);
        return DefaultRSMapper.of(deleteDTO);
    }

    public DefaultRS<?> recoverPostById(int id) {
        return DefaultRSMapper.of(new PostDTO());
    }

    public DefaultRS<?> getCommentsToPost(int id, Pageable pageable) {
        List<PostComment> comments = commentRepository.findByPostId(id, pageable);
        List<CommentDTO> commentsDTO = comments.stream()
                .map(commentMapper::mapToCommentDTO)
                .collect(Collectors.toList());
        return DefaultRSMapper.of(commentsDTO);
    }


    public DefaultRS<?> makeCommentToPost(int postId, CommentRQ commentRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Post post = postRepository.findPostById(postId).orElseThrow(BadRequestException::new);
        PostComment postComment = createPostComment(commentRQ, currentPerson, post);
        CommentDTO commentDTO = commentMapper.mapToCommentDTO(postComment);
        return DefaultRSMapper.of(commentDTO);
    }

    public DefaultRS<?> rewriteCommentToThePost(int id, int commentId, CommentRQ commentRQ) {
        PostComment postComment = commentRepository.findById(commentId).orElseThrow(BadRequestException::new);
        postComment.setCommentText(commentRQ.getCommentText());
        commentRepository.save(postComment);
        CommentDTO commentDTO = commentMapper.mapToCommentDTO(postComment);
        return DefaultRSMapper.of(commentDTO);
    }


    public DefaultRS<?> deleteCommentToThePost(int id, int commentId) {
        PostComment postComment = commentRepository.findById(commentId).orElseThrow(BadRequestException::new);
        commentRepository.delete(postComment);
        DeleteDTO deleteDTO = new DeleteDTO(id);
        return DefaultRSMapper.of(deleteDTO);
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

    private long checkDate(long dateTo) {
        if (dateTo == 0) {
            dateTo = new Date().getTime();
        }
        return dateTo;
    }

    private void changePostPublishDate(long publishDate, Post post) {
        if (publishDate != 0) {
            post.setTime(new Timestamp(publishDate));
        }
    }

    private void changePostTexts(PostChangeRQ postChangeRQ, Post post) {
        if (!postChangeRQ.getPostText().isEmpty()) {
            post.setPostText(postChangeRQ.getPostText());
        }
        if (!postChangeRQ.getTitle().isEmpty()) {
            post.setTitle(postChangeRQ.getTitle());
        }
    }

    private PostComment createPostComment(CommentRQ commentRQ, Person currentPerson, Post post) {
        PostComment postComment = new PostComment();
        postComment.setCommentText(commentRQ.getCommentText());
        if (commentRQ.getParentId() != null) {
            PostComment parentComment = commentRepository.findById(commentRQ.getParentId()).orElseThrow(BadRequestException::new);
            postComment.setParent(parentComment);
        }
        postComment.setPost(post);
        postComment.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        postComment.setAuthor(currentPerson);
        commentRepository.save(postComment);
        return postComment;
    }

}
