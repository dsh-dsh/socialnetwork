package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.*;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.repository.CommentRepository;
import com.skillbox.socialnet.repository.PostRepository;
import com.skillbox.socialnet.repository.Tag2PostRepository;
import com.skillbox.socialnet.repository.TagRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final FriendsService friendsService;
    private final AuthService authService;
    private final Tag2PostRepository tag2PostRepository;
    private final TagRepository tagRepository;
    private final PersonService personService;

    public GeneralListResponse<?> searchPosts(PostSearchRQ postSearchRQ, Pageable pageable) {
        long dateTo = checkDate(postSearchRQ.getDateTo());
        Page<Post> postPage = getPostsPage(postSearchRQ, pageable, dateTo);
        List<PostDTO> postsDTOList = getPostDTOList(postPage.getContent());

        return new GeneralListResponse<>(postsDTOList, postPage);
    }

    private Page<Post> getPostsPage(PostSearchRQ postSearchRQ, Pageable pageable, long dateTo) {
        Page<Post> postPage;
        if(postSearchRQ.getTags().size() > 0) {
            postPage = postRepository.findPostWithTags(
                    postSearchRQ.getAuthor(), postSearchRQ.getText(),
                    new Timestamp(postSearchRQ.getDateFrom()), new Timestamp(dateTo),
                    postSearchRQ.getTags(), pageable);
        } else {
            postPage = postRepository.findPost(
                    postSearchRQ.getAuthor(), postSearchRQ.getText(),
                    new Timestamp(postSearchRQ.getDateFrom()), new Timestamp(dateTo), pageable);
        }

        return postPage;
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

    public PostDTO addPostToUserWall(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Person person = personService.getPersonById(id);
        Post post = new Post();
        post.setAuthor(person);
        post.setTitle(postChangeRQ.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        post.setTime(new Timestamp((publishDate == 0) ? Calendar.getInstance().getTimeInMillis() : publishDate));
        post = postRepository.save(post);
        addTags2Post(post, postChangeRQ.getTags());

        return getPostDTO(post);
    }

    public PostDTO changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        changePostPublishDate(publishDate, post);
        changePostTexts(postChangeRQ, post);
        postRepository.save(post);
        addTags2Post(post, postChangeRQ.getTags());

        return getPostDTO(post);
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

    public DeleteDTO deletePostById(int id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        postRepository.delete(post);

        return new DeleteDTO(id);
    }

    public List<PostDTO> getUserWall(int id, Pageable pageable) {
        Person person = personService.getPersonById(id);
        Page<Post> postPage = postRepository.findPostsByAuthor(person, pageable);
        List<PostDTO> postDTOs = getPostDTOList(postPage.getContent());

        return postDTOs;
    }

    public PostDTO recoverPostById(int id) {
        return new PostDTO();
    }

    public GeneralListResponse<?> getCommentsToPost(int id, Pageable pageable) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        Page<PostComment> commentPage = commentRepository.findByPostAndIsBlocked(post, false, pageable);
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

    private PostDTO getPostDTO(Post post) {
        PostDTO postDTO = PostDTO.getPostDTO(post);
        postDTO.setMyLike(getMyLike(post.getLikes()));
        return postDTO;
    }

    private int getMyLike(List<PostLike> likes) {
        Person me = authService.getPersonFromSecurityContext();
        return (int)likes.stream().filter(like -> like.getPerson().equals(me)).count();
    }

    private void addTags2Post(Post post, List<String> tagNames) {
        List<Tag> tags = addTagsIfNotExists(tagNames);
        Set<Post2tag> newTagPosts = tags.stream()
                .map(tag -> new Post2tag(post, tag))
                .collect(Collectors.toSet());
        post.getTags().clear();
        post.getTags().addAll(newTagPosts);
        postRepository.save(post);
    }

    private List<Tag> addTagsIfNotExists(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTag(tagName)
                    .orElseGet(() -> createNewTag(tagName));
            tags.add(tag);
        }

        return tags;
    }

    private Tag createNewTag(String tagName) {
        Tag tag = new Tag();
        tag.setTag(tagName);

        return tagRepository.save(tag);
    }

    private List<PostDTO> getPostDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::getPostDTO)
                .collect(Collectors.toList());
    }

    private long checkDate(long dateTo) {
        if (dateTo == 0) {
            dateTo = new Date().getTime();
        }

        return dateTo;
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
