package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.rq.CommentRQ;
import com.skillbox.socialnet.model.rq.PostChangeRQ;
import com.skillbox.socialnet.model.rq.PostSearchRQ;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.*;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.PostRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final FriendsService friendsService;
    private final AuthService authService;
    private final PersonService personService;
    private final TagService tagService;
    private final NotificationService notificationService;
    private final FriendshipRepository friendshipRepository;

    public static final Logger logger = LogManager.getLogger(PostService.class);

    public GeneralListResponse<PostDTO> searchPosts(PostSearchRQ postSearchRQ, Pageable pageable) {
        long dateTo = checkDate(postSearchRQ.getDateTo());
        Page<Post> postPage = getPostsPage(postSearchRQ, pageable, dateTo);
        List<PostDTO> postsDTOList = getPostDTOList(postPage.getContent());

        return new GeneralListResponse<>(postsDTOList, postPage);
    }

    public GeneralListResponse<PostDTO> getFeeds(ElementPageable pageable) {
        pageable.setSort(Sort.by("time").descending());
        List<Person> friends = getFriendList();
        Page<Post> postPage = postRepository.findByAuthorIn(friends, pageable);
        List<Post> posts = addPostsToLimit(postPage);
        List<PostDTO> postDTOs = getPostDTOList(posts);

        return new GeneralListResponse<>(postDTOs, postPage);
    }

    private List<Person> getFriendList() {
        List<Person> friends = friendsService.getMyNotBlockedFriends();
        if(friends.isEmpty()) {
            Person me = authService.getPersonFromSecurityContext();
            friends = List.copyOf(friendsService.getRecommendedFriends(me, Set.of()));
        }
        return friends;
    }

    public PostDTO getPostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);

        return getPostDTO(post);
    }

    public PostDTO addPostToUserWall(int personId, long publishDate, PostChangeRQ postChangeRQ) {
        Person person = personService.getPersonById(personId);
        Post post = new Post();
        post.setAuthor(person);
        post.setTitle(postChangeRQ.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        post.setTime(new Timestamp((publishDate == 0) ? Calendar.getInstance().getTimeInMillis() : publishDate));
        post = postRepository.save(post);
        tagService.addTags2Post(post, postChangeRQ.getTags());
        createNotificationsForWall(personId);
        return getPostDTO(post);
    }

    public PostDTO changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        changePostPublishDate(publishDate, post);
        changePostTexts(postChangeRQ, post);
        postRepository.save(post);
        tagService.addTags2Post(post, postChangeRQ.getTags());

        return getPostDTO(post);
    }

    public DeleteDTO deletePostById(int id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        postRepository.delete(post);

        return new DeleteDTO(id);
    }

    public GeneralListResponse<PostDTO> getUserWall(int postId, String type, ElementPageable pageable) {
        Person person = personService.getPersonById(postId);
        Page<Post> postPage = getPostPage(type, pageable, person);
        List<PostDTO> postDTOList = getPostDTOList(postPage.getContent());

        return new GeneralListResponse<>(postDTOList, postPage);
    }

    private Page<Post> getPostPage(String type, ElementPageable pageable, Person person) {
        pageable.setSort(Sort.by("time").descending());
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        if (type.equals("posted")) {
            return postRepository.findByAuthorAndTimeBefore(person, now, pageable);
        } else if (type.equals("queued")){
            return postRepository.findByAuthorAndTimeAfter(person, now, pageable);
        } else {
            return postRepository.findPostsByAuthor(person, pageable);
        }
    }

    public List<CommentDTO> getCommentsToPost(int id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> new BadRequestException(Constants.NO_SUCH_POST_MESSAGE));

        return commentService.getCommentsDTOList(post);
    }

    public CommentDTO makeCommentToPost(int postId, CommentRQ commentRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new BadRequestException(Constants.NO_SUCH_POST_MESSAGE));
        PostComment postComment = commentService.createPostComment(commentRQ, currentPerson, post);
        createNotificationForComment(postId, commentRQ.getParentId()==null, currentPerson);
        return CommentDTO.getCommentDTO(postComment);
    }

    public List<Post> addPostsToLimit(Page<Post> postPage) {
        int postLimit = postPage.getPageable().getPageSize();
        List<Post> posts = postPage.getContent();
        List<Post> postList = new ArrayList<>(posts);
        if (postPage.getPageable().getOffset() == 0 && posts.size() < postLimit) {
            int limit = postLimit - posts.size();
            List<Person> persons = getAuthorsToExclude(posts);
            List<Post> additionalPosts = postRepository
                    .findOrderByNewAuthorsExclude(persons, PageRequest.of(0, limit));
            postList.addAll(additionalPosts);
            postList = postList.stream()
                    .sorted(Comparator.comparing(Post::getTime).reversed())
                    .collect(Collectors.toList());
        }

        return postList;
    }

    private List<Person> getAuthorsToExclude(List<Post> posts) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        List<Person> persons = posts.stream()
                .map(Post::getAuthor)
                .collect(Collectors.toList());
        persons.add(currentPerson);
        persons.addAll(friendsService.getBlockedFriends(currentPerson));
        return persons;
    }

    private Page<Post> getPostsPage(PostSearchRQ postSearchRQ, Pageable pageable, long dateTo) {
        Page<Post> postPage;
        if (!postSearchRQ.getTags().isEmpty()) {
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

    private PostDTO getPostDTO(Post post) {
        PostDTO postDTO = PostDTO.getPostDTO(post);
        postDTO.setMyLike(getMyLike(post.getLikes()));
        postDTO.setComments(commentService.getCommentsDTOList(post));
        postDTO.getAuthor().setMe(postDTO.getAuthor().getEmail().equals(authService.getPersonFromSecurityContext().getEMail()));
        return postDTO;
    }

    private int getMyLike(List<PostLike> likes) {
        Person me = authService.getPersonFromSecurityContext();
        return (int) likes.stream().filter(like -> like.getPerson().equals(me)).count();
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

    private void createNotificationForComment(int postId, boolean isPost, Person person) {
        List<Integer> dstPersonIds = postRepository.getIdsForPostNotifications(postId, person.getId());
        NotificationTypeCode type;
        if(isPost){
            type = NotificationTypeCode.POST_COMMENT;
        } else {
            type = NotificationTypeCode.COMMENT_COMMENT;
        }
        for (Integer dstPersonId : dstPersonIds) {
            notificationService.createNewNotification(
                    type,
                    dstPersonId,
                    person.getId(),
                    personService.getPersonById(dstPersonId).getEMail());

        }
    }

    private void createNotificationsForWall(int personId) {
        List<NotificationInterfaceProjectile> ids = friendshipRepository.getIdsForNotification(personId);
        int currentPersonId = authService.getPersonFromSecurityContext().getId();
        for (NotificationInterfaceProjectile nip : ids) {
            int dstPersonId;
            if(nip.getSrc() != personId){
                dstPersonId = nip.getSrc();
            } else {
                dstPersonId = nip.getDst();
            }
            notificationService.createNewNotification(
                    NotificationTypeCode.POST,
                    dstPersonId,
                    currentPersonId,
                    personService.getPersonById(dstPersonId).getEMail());
        }
    }

}
