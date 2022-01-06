package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
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
import com.skillbox.socialnet.repository.Tag2PostRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FriendsService friendsService;
    private final AuthService authService;
    private final  Tag2PostRepository tag2PostRepository;

    public GeneralListResponse<?> searchPosts(PostSearchRQ postSearchRQ, Pageable pageable) {
        long dateTo = checkDate(postSearchRQ.getDateTo());
        setEmptyTagsToNull(postSearchRQ);
        Page<Post> postPage = postRepository.findPost(
                postSearchRQ.getAuthor(), postSearchRQ.getText(),
                new Timestamp(postSearchRQ.getDateFrom()), new Timestamp(dateTo),
                postSearchRQ.getTags(), pageable);
        List<PostDTO> postsDTOList = postPage.stream()
                .map(this::getPostDTO)
                .collect(Collectors.toList());
        return new GeneralListResponse<>(postsDTOList, postPage);
    }

    // FIXME если тегов нет фронт посылает пустой массив, найти решение проверки :tags is empty в hql
    private void setEmptyTagsToNull(PostSearchRQ postSearchRQ) {
        if(postSearchRQ.getTags() != null) {
            if (postSearchRQ.getTags().size() == 0) {
                postSearchRQ.setTags(null);
            }
        }
    }

    public GeneralListResponse<?> getFeeds(Pageable pageable) {
        List<Person> friends = friendsService.getMyFriends();
        Page<Post> postPage = postRepository.findByAuthorIn(friends, pageable);
        List<Post> posts = addPostsToLimit(postPage.getContent());
        List<PostDTO> postDTOs = getPostDTOList(posts);
        return new GeneralListResponse<>(postDTOs, postPage);
    }

    private List<Post> addPostsToLimit(List<Post> posts) {
        List<Post> postList = new ArrayList<>(posts);
        if(posts.size() < Constants.RECOMMENDED_POST_LIMIT) {
            int limit = Constants.RECOMMENDED_POST_LIMIT - posts.size();
            List<Post> additionalPosts = postRepository
                    .findOrderByNewAuthorsExclude(posts, PageRequest.of(0, limit));
            postList.addAll(additionalPosts);
        }
        return postList;
    }

    public PostDTO getPostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);
        return getPostDTO(post);
    }

    public PostDTO changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        changePostPublishDate(publishDate, post);
        changePostTexts(postChangeRQ, post);
        postRepository.save(post);
        return getPostDTO(post);
    }


    public DeleteDTO deletePostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);
//            postRepository.delete(post); // TODO сначала удалять все что ссылается на этот post
        return new DeleteDTO(id);
    }

    public PostDTO recoverPostById(int id) {
        return new PostDTO();
    }

    public GeneralListResponse<?> getCommentsToPost(int id, Pageable pageable) {
        Page<PostComment> commentPage = commentRepository.findByPostIdPageable(id, pageable);
        List<CommentDTO> commentsDTO = commentPage.stream()
                .map(CommentDTO::getCommentDTO)
                .collect(Collectors.toList());
        return new GeneralListResponse<>(commentsDTO, commentPage);
    }


    public CommentDTO makeCommentToPost(int postId, CommentRQ commentRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Post post = postRepository.findPostById(postId)
                .orElseThrow(BadRequestException::new);
        PostComment postComment = createPostComment(commentRQ, currentPerson, post);
        return CommentDTO.getCommentDTO(postComment);
    }

    public CommentDTO rewriteCommentToThePost(int id, int commentId, CommentRQ commentRQ) {
        PostComment postComment = commentRepository.findById(commentId)
                .orElseThrow(BadRequestException::new);
        postComment.setCommentText(commentRQ.getCommentText());
        commentRepository.save(postComment);
        return CommentDTO.getCommentDTO(postComment);
    }


    public DeleteDTO deleteCommentToThePost(int id, int commentId) {
        PostComment postComment = commentRepository.findById(commentId)
                .orElseThrow(BadRequestException::new);
        commentRepository.delete(postComment);
        return new DeleteDTO(id);
    }

    public CommentDTO recoverCommentToPost(int id, int commentId) {
        return new CommentDTO();
    }

    public MessageOkDTO reportPostById(int id) {
        return new MessageOkDTO();
    }

    public DefaultRS<?> reportCommentToThePost(int id, int commentId) {
        return DefaultRSMapper.of(new MessageOkDTO());
    }

    private List<PostDTO> getPostDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::getPostDTO)
                .collect(Collectors.toList());
    }

    private PostDTO getPostDTO(Post post) {
        return PostDTO.getPostDTO(
                post,
                tag2PostRepository.getAllByPost(post),
                commentRepository.findByPost(post));
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
            PostComment parentComment = commentRepository.findById(commentRQ.getParentId())
                    .orElseThrow(BadRequestException::new);
            postComment.setParent(parentComment);
        }
        postComment.setPost(post);
        postComment.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        postComment.setAuthor(currentPerson);
        commentRepository.save(postComment);
        return postComment;
    }

}
