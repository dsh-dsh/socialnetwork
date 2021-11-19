package com.skillbox.socialnet.service;



import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.entity.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PostService {



    public DefaultRS getPostsByText(String text, long dateFrom, long dateTo, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());

        defaultRS.setData(getFakePostDTO());
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


    public DefaultRS getPostById(Long id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getPostDTO());
        return defaultRS;

    }

    public DefaultRS changePostById(int id, long publishDate, PostChangeRQ postChangeRQ) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getPostDTO());
        return defaultRS;

    }

    public DefaultRS deletePostById(int id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(id);
        defaultRS.setData(locationDTO);
        return defaultRS;

    }


    public DefaultRS recoverPostById(int id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getPostDTO());
        return defaultRS;
    }

    public DefaultRS getCommentsToPost(int id, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTOList());
        return defaultRS;
    }


    public DefaultRS makeCommentToPost(int id, CommentRQ commentRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTO());
        return defaultRS;
    }

    public DefaultRS rewriteCommentToThePost(int id, int commentId, CommentRQ commentRQ) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTO());
        return defaultRS;
    }


    public DefaultRS deleteCommentToThePost(int id, int commentId) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(id);
        defaultRS.setData(locationDTO);
        return defaultRS;

    }

    public DefaultRS recoverCommentToPost(int id, int commentId) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTO());
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

    private List<CommentDTO> getCommentDTOList() {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(getCommentDTO());
        return commentDTOList;
    }


    private CommentDTO getCommentDTO(){
        CommentDTO commentDTO = new CommentDTO();
        return commentDTO;
    }

    private List<PostDTO> getPostsDTOList(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();
        postList.forEach(post -> {
            postDTOList.add(getPostDTO(post));
        });
        return postDTOList;
    }

    private PostDTO getPostDTO(Post post)
    {
        List<PostDTO> postDTOList = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setPostText(post.getPostText());
        postDTO.setTitle(post.getTitle());
//        postDTO.setTime(post.getTime());
        postDTO.setBlocked(post.isBlocked());
        postDTO.setComments(getCommentDTOList());
        postDTO.setAuthor(new UserDTO());
        return postDTO;
    }


    private PostDTO getPostDTO()
    {
        PostDTO postDTO = new PostDTO();
        return postDTO;
    }


    private List<PostDTO> getFakePostDTO()
    {
        List<PostDTO> postDTOList = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setPostText("post 2 is here");
        postDTO.setTitle("and its title too");
        postDTO.setTime(Calendar.getInstance().getTimeInMillis());
        postDTO.setBlocked(false);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1);
        userDTO1.setFirstName("user");
        userDTO1.setLastName(" 2");
        userDTO1.setPhoto("a tut jpg");

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

        postDTOList.add(postDTO2);
        postDTOList.add(postDTO);

        return postDTOList;
    }





}
