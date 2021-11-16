package com.skillbox.socialnet.service;


import com.skillbox.socialnet.model.RQ.CommentRequest;
import com.skillbox.socialnet.model.RQ.PostChangeRequest;
import com.skillbox.socialnet.model.response.DefaultResponse;
import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PostService {


    public DefaultResponse getPostsByText(String text, long date_from, long date_to, int offset, int itemPerPage) {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setOffset(offset);
        defaultResponse.setPerPage(itemPerPage);
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        List<PostDTO> posts = new ArrayList<>();
        posts.add(getPostDTO());
        defaultResponse.setData(posts);
        return defaultResponse;

    }


    public DefaultResponse getPostById(Long id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getPostDTO());
        return defaultResponse;

    }

    private PostDTO getPostDTO() {
        return new PostDTO();
    }

    public DefaultResponse changePostById(long id, long publish_date, PostChangeRequest postChangeRequest) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getPostDTO());
        return defaultResponse;

    }

    public DefaultResponse deletePostById(long id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(id);
        defaultResponse.setData(locationDTO);
        return defaultResponse;

    }


    public DefaultResponse recoverPostById(long id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getPostDTO());
        return defaultResponse;
    }

    public DefaultResponse getCommentsToPost(long id, int offset, int itemPerPage) {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getCommentDTOList());
        return defaultResponse;
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

    public DefaultResponse makeCommentToPost(long id, CommentRequest commentRequest) {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getCommentDTO());
        return defaultResponse;
    }

    public DefaultResponse rewriteCommentToThePost(long id, long comment_id, CommentRequest commentRequest) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getCommentDTO());
        return defaultResponse;
    }


    public DefaultResponse deleteCommentToThePost(long id, long comment_id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(id);
        defaultResponse.setData(locationDTO);
        return defaultResponse;

    }

    public DefaultResponse recoverCommentToPost(long id, long comment_id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(getCommentDTO());
        return defaultResponse;
    }

    public DefaultResponse reportPostById(String id) {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(new MessageDTO());
        return defaultResponse;

    }

    public DefaultResponse reportCommentToThePost(long id, long comment_id) {

        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultResponse.setData(new MessageDTO());
        return defaultResponse;
    }
}
