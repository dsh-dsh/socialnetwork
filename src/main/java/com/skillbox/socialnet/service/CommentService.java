package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.DeleteDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.rq.CommentRQ;
import com.skillbox.socialnet.repository.CommentRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentDTO rewriteCommentToThePost(int commentId, CommentRQ commentRQ) {
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

    public PostComment createPostComment(CommentRQ commentRQ, Person currentPerson, Post post) {
        PostComment postComment = new PostComment();
        postComment.setCommentText(commentRQ.getCommentText());
        if (commentRQ.getParentId() != null) {
            PostComment parentComment = commentRepository.findById(commentRQ.getParentId())
                    .orElseThrow(() -> new BadRequestException(Constants.NO_SUCH_COMMENT_MESSAGE));
            postComment.setParent(parentComment);
        }
        postComment.setPost(post);
        postComment.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        postComment.setAuthor(currentPerson);
        commentRepository.save(postComment);

        return postComment;
    }

    public List<CommentDTO> getCommentsDTOList(Post post) {
        List<PostComment> comments = commentRepository.findByPostAndParentAndIsBlocked(post, null, false);
        return comments.stream()
                .map(this::getCommentDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO getCommentDTO(PostComment comment) {
        CommentDTO commentDTO = CommentDTO.getCommentDTO(comment);
        commentDTO.setSubComments(getSubCommentsDTO(comment));
        return commentDTO;
    }

    private List<CommentDTO> getSubCommentsDTO(PostComment comment) {
        List<PostComment> comments = getSubComments(comment);
        return comments.stream()
                .map(CommentDTO::getCommentDTO)
                .collect(Collectors.toList());
    }

    private List<PostComment> getSubComments(PostComment parent) {
        return commentRepository.findByParent(parent);
    }
}
