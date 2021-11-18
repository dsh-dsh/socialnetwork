package com.skillbox.socialnet.service;



import com.skillbox.socialnet.model.RQ.CommentRQ;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.RS.DefaultRS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PostService {


    public DefaultRS getPostsByText(String text, long date_from, long date_to, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        List<PostDTO> posts = new ArrayList<>();
        posts.add(getPostDTO());
        defaultRS.setData(posts);
        return defaultRS;

    }


    public DefaultRS getPostById(Long id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getPostDTO());
        return defaultRS;

    }

    private PostDTO getPostDTO() {
        return new PostDTO();
    }

    public DefaultRS changePostById(int id, long publish_date, PostChangeRQ postChangeRQ) {

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


    private List<CommentDTO> getCommentDTOList() {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(getCommentDTO());
        return commentDTOList;
    }


    private CommentDTO getCommentDTO(){
        CommentDTO commentDTO = new CommentDTO();
        return commentDTO;
    }

    public DefaultRS makeCommentToPost(int id, CommentRQ commentRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTO());
        return defaultRS;
    }

    public DefaultRS rewriteCommentToThePost(int id, int comment_id, CommentRQ commentRQ) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getCommentDTO());
        return defaultRS;
    }


    public DefaultRS deleteCommentToThePost(int id, int comment_id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(id);
        defaultRS.setData(locationDTO);
        return defaultRS;

    }

    public DefaultRS recoverCommentToPost(int id, int comment_id) {

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

    public DefaultRS reportCommentToThePost(int id, int comment_id) {

        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(new MessageDTO());
        return defaultRS;
    }
}
