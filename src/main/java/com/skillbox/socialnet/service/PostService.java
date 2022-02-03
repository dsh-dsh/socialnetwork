package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.PostSearchRQ;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
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
        List<Person> friends = getFriendList();
        pageable.setSort(Sort.by("time").descending());
        Page<Post> postPage = postRepository.findByAuthorIn(friends, pageable);
        List<Post> posts = addPostsToLimit(postPage.getContent());
        List<PostDTO> postDTOs = getPostDTOList(posts);

        return new GeneralListResponse<>(postDTOs, postPage);
    }

    private List<Person> getFriendList() {
        List<Person> friends = friendsService.getMyFriends();
        if(friends.size() == 0) {
            Person me = authService.getPersonFromSecurityContext();
            friends = List.copyOf(friendsService.getRecommendedFriends(me, Set.of()));
        }
        return friends;
    }

    public PostDTO getPostById(int id) {
        Post post = postRepository.findPostById(id).orElseThrow(BadRequestException::new);

        return getPostDTO(post);
    }

    public void add100Posts() {
        for (int i = 0; i < 100; i++) {
            PostChangeRQ data = new PostChangeRQ();
            data.setTitle("title " + i);
            data.setPostText("text text text text text " + i);
            data.setTags(List.of("tag" + 1, "Spring", "Java"));
//            long publishDate = Calendar.getInstance().getTimeInMillis();
            addPostToUserWall(10, 0, data);
        }
    }

    public PostDTO addPostToUserWall(int personId, long publishDate, PostChangeRQ postChangeRQ) {
        Person person = personService.getPersonById(personId);
        Post post = new Post();
        post.setAuthor(person);
        post.setTitle(postChangeRQ.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        post.setTime(new Timestamp((publishDate == 0) ? Calendar.getInstance().getTimeInMillis() : publishDate));
        post = postRepository.save(post);
        addTags2Post(post, postChangeRQ.getTags());
        createNotificationsForWall(personId);
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

    public DeleteDTO deletePostById(int id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        postRepository.delete(post);

        return new DeleteDTO(id);
    }

    public List<PostDTO> getUserWall(int id, Pageable pageable) {
        Person person = personService.getPersonById(id);
        Page<Post> postPage = postRepository.findPostsByAuthor(person, pageable);

        return getPostDTOList(postPage.getContent());
    }

    public GeneralListResponse<CommentDTO> getCommentsToPost(int id, Pageable pageable) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(BadRequestException::new);
        List<CommentDTO> commentsDTO = commentService.getCommentsDTOList(post);

        return new GeneralListResponse<>(commentsDTO, pageable);
    }

    public CommentDTO makeCommentToPost(int postId, CommentRQ commentRQ) {
        Person currentPerson = authService.getPersonFromSecurityContext();
        Post post = postRepository.findPostById(postId).orElseThrow(BadRequestException::new);
        PostComment postComment = commentService.createPostComment(commentRQ, currentPerson, post);
        createNotificationForComment(postId, commentRQ.getParentId()==null, currentPerson);
        return CommentDTO.getCommentDTO(postComment);
    }

    private List<Post> addPostsToLimit(List<Post> posts) {
        List<Post> postList = new ArrayList<>(posts);
        if (posts.size() < Constants.RECOMMENDED_POST_LIMIT) {
            int limit = Constants.RECOMMENDED_POST_LIMIT - posts.size();
            List<Post> additionalPosts = postRepository
                    .findOrderByNewAuthorsExclude(posts, PageRequest.of(0, limit));
            postList.addAll(additionalPosts);
            postList = postList.stream()
                    .sorted(Comparator.comparing(Post::getTime).reversed())
                    .collect(Collectors.toList());
        }

        return postList;
    }

    private Page<Post> getPostsPage(PostSearchRQ postSearchRQ, Pageable pageable, long dateTo) {
        Page<Post> postPage;
        if (postSearchRQ.getTags().size() > 0) {
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

    private void addTags2Post(Post post, List<String> tagNames) {
        List<Tag> tags = tagService.addTagsIfNotExists(tagNames);
        Set<Post2tag> newTagPosts = tagService.getPost2tagSet(post, tags);
        post.getTags().clear();
        post.getTags().addAll(newTagPosts);
        postRepository.save(post);
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
