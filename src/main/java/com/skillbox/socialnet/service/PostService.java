package com.skillbox.socialnet.service;



import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.entity.PostLike;
import com.skillbox.socialnet.repository.CommentRepository;
import com.skillbox.socialnet.repository.LikesRepository;
import com.skillbox.socialnet.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likesRepository = likesRepository;
    }


    public DefaultRS getPostsByText(String text, long dateFrom, long dateTo, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        List<Post> postList;
        if (dateFrom == 0){
            postList = postRepository.findPostByPostText(getPageable(offset, itemPerPage), text).getContent();
        }
        else{
            postList = postRepository.findPostByPostTextAndTimeBetween(getPageable(offset, itemPerPage), new Timestamp(dateFrom), new Timestamp(dateTo), text).getContent();
        }
        List<PostDTO> postsDTOList = getPostsDTOList(postList);
        defaultRS.setData(postsDTOList);
        return defaultRS;

    }


    public DefaultRS getFeeds(String name, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());

        defaultRS.setData(getFakePostDTO());
        return defaultRS;

    }


    public DefaultRS getPostById(int id) {

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

    public DefaultRS changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {

        DefaultRS defaultRS = new DefaultRS();
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


    public DefaultRS recoverPostById(int id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new PostDTO());
        return defaultRS;
    }

    public DefaultRS getCommentsToPost(int id, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTOList(commentRepository.findByPostId(getPageable(offset, itemPerPage), id)));
        return defaultRS;
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

    public DefaultRS recoverCommentToPost(int id, int commentId) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new CommentDTO());
        return defaultRS;
    }

    public DefaultRS reportPostById(int id) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;

    }

    public DefaultRS reportCommentToThePost(int id, int commentId) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
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

    private List<PostDTO> getPostsDTOList(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();
        postList.forEach(post -> {
            PostDTO postDTO = getPostDTO(post);
            postDTOList.add(postDTO);

        });
        return postDTOList;
    }

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


    private PostDTO getPostDTO()
    {
        PostDTO postDTO = new PostDTO();
        return postDTO;
    }


    private List<PostDTO> getFakePostDTO()
    {
        List<PostDTO> postDTOList = new ArrayList<>();
        UserDTO userDTO3 = new UserDTO();
        userDTO3.setId(1);
        userDTO3.setFirstName("user");
        userDTO3.setLastName(" 3");
        userDTO3.setPhoto("a tut jpg");



        PostDTO postDTO3 = new PostDTO();
        postDTO3.setId(1);
        postDTO3.setPostText("The simplest option we have is to use a spring.liquibase.enabled property. This way, all the remaining Liquibase configuration stays untouched.");
        postDTO3.setTitle("title 3");
        postDTO3.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO3.setBlocked(false);


        postDTO3.setAuthor(userDTO3);

        CommentDTO commentDTO5 = new CommentDTO();
        commentDTO5.setId(0);
        commentDTO5.setCommentText("baaad comment");
        commentDTO5.setAuthorId(3);

        CommentDTO commentDTO6 = new CommentDTO();
        commentDTO6.setCommentText("sam takoi");
        commentDTO6.setAuthorId(4);
        commentDTO6.setId(1);
        commentDTO6.setParentId(0);

        List<CommentDTO> commentDTOList3 = new ArrayList<>();
        commentDTOList3.add(commentDTO5);
        commentDTOList3.add(commentDTO6);
        postDTO3.setComments(commentDTOList3);

        PostDTO postDTO2 = new PostDTO();
        postDTO2.setId(1);
        postDTO2.setPostText("cool post u know");
        postDTO2.setTitle("very cool title");
        postDTO2.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO2.setBlocked(false);
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(1);
        userDTO2.setFirstName("user");
        userDTO2.setLastName(" 1");
        userDTO2.setPhoto("tut png");

        postDTO2.setAuthor(userDTO2);

        CommentDTO commentDTO3 = new CommentDTO();
        commentDTO3.setCommentText("cool comment");
        commentDTO3.setAuthorId(2);
        commentDTO3.setId(2);

        CommentDTO commentDTO4 = new CommentDTO();
        commentDTO4.setCommentText("tut vse comment cool");
        commentDTO4.setAuthorId(1);
        commentDTO4.setId(4);

        List<CommentDTO> commentDTOList1 = new ArrayList<>();
        commentDTOList1.add(commentDTO3);
        commentDTOList1.add(commentDTO4);
        postDTO2.setComments(commentDTOList1);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1);
        userDTO1.setFirstName("user");
        userDTO1.setLastName(" 2");
        userDTO1.setPhoto("a tut jpg");


        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setPostText("post 2 is here");
        postDTO.setTitle("and its title too");
        postDTO.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO.setBlocked(false);


        postDTO.setAuthor(userDTO1);

        CommentDTO commentDTO1 = new CommentDTO();
        commentDTO1.setId(0);
        commentDTO1.setCommentText("baaad comment");
        commentDTO1.setAuthorId(3);

        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO2.setCommentText("sam takoi");
        commentDTO2.setAuthorId(4);
        commentDTO2.setId(1);
        commentDTO2.setParentId(0);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(commentDTO1);
        commentDTOList.add(commentDTO2);
        postDTO.setComments(commentDTOList);



        PostDTO postDTO5 = new PostDTO();
        postDTO5.setId(1);
        postDTO5.setPostText("If we're using Spring Boot, there is no need to define a bean for Liquibase, but we still need to make sure we add the liquibase-core dependency.");
        postDTO5.setTitle("title 5");
        postDTO5.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO5.setBlocked(false);


        postDTO5.setAuthor(userDTO1);


        List<CommentDTO> commentDTOList4 = new ArrayList<>();
        commentDTOList4.add(commentDTO1);
        commentDTOList4.add(commentDTO2);
        postDTO5.setComments(commentDTOList4);


        PostDTO postDTO6 = new PostDTO();
        postDTO6.setId(1);
        postDTO6.setPostText("Our first option to run the changes on application startup is via a Spring bean. There are of course many other ways, but if we're dealing with a Spring application – this is a good, simple way to go:");
        postDTO6.setTitle("title 6");
        postDTO6.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO6.setBlocked(false);


        postDTO6.setAuthor(userDTO1);
        postDTO6.setComments(commentDTOList4);

        PostDTO postDTO7 = new PostDTO();
        postDTO7.setId(1);
        postDTO7.setPostText("In this quick tutorial, we'll make use of Liquibase to evolve the database schema of a Java web application.\n" +
                "\n" +
                "We're going to focus on a general Java app first, and we're also going to take a focused look at some interesting options available for Spring and Hibernate.\n" +
                "\n" +
                "Very briefly, the core of using Liquibase is the changeLog file – an XML file that keeps track of all changes that need to run to update the DB.");
        postDTO7.setTitle("title 7");
        postDTO7.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO7.setBlocked(false);


        postDTO7.setAuthor(userDTO1);
        postDTO7.setComments(commentDTOList4);


        postDTOList.add(postDTO2);
        postDTOList.add(postDTO3);
        postDTOList.add(postDTO);
        postDTOList.add(postDTO6);
        postDTOList.add(postDTO5);
        postDTOList.add(postDTO7);

        return postDTOList;
    }

    public PostDTO getFakePost(){
        PostDTO postDTO2 = new PostDTO();
        postDTO2.setId(1);
        postDTO2.setPostText("cool post u know");
        postDTO2.setTitle("very cool title");
        postDTO2.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO2.setBlocked(false);
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(1);
        userDTO2.setFirstName("user");
        userDTO2.setLastName(" 1");
        userDTO2.setPhoto("tut png");

        postDTO2.setAuthor(userDTO2);

        CommentDTO commentDTO3 = new CommentDTO();
        commentDTO3.setCommentText("cool comment");
        commentDTO3.setAuthorId(2);
        commentDTO3.setId(2);

        CommentDTO commentDTO4 = new CommentDTO();
        commentDTO4.setCommentText("tut vse comment cool");
        commentDTO4.setAuthorId(1);
        commentDTO4.setId(4);

        List<CommentDTO> commentDTOList1 = new ArrayList<>();
        commentDTOList1.add(commentDTO3);
        commentDTOList1.add(commentDTO4);
        postDTO2.setComments(commentDTOList1);
        return postDTO2;

    }

    public List<CommentDTO> getFakeCommentList(){
        CommentDTO commentDTO3 = new CommentDTO();
        commentDTO3.setCommentText("cool comment");
        commentDTO3.setAuthorId(2);
        commentDTO3.setId(2);

        CommentDTO commentDTO4 = new CommentDTO();
        commentDTO4.setCommentText("tut vse comment cool");
        commentDTO4.setAuthorId(1);
        commentDTO4.setId(4);

        List<CommentDTO> commentDTOList1 = new ArrayList<>();
        commentDTOList1.add(commentDTO3);
        commentDTOList1.add(commentDTO4);
        return commentDTOList1;
    }

    private Pageable getPageable(int offset, int itemPerPage){
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        return pageable;
    }







}
