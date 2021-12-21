package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.PostComment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PostCommentMapper {
    private final ModelMapper modelMapper;

    private final Converter<PostComment, Integer> parentConverter =
            parent -> parent.getSource().getId();

    private final Converter<Timestamp, Long> timestampConverter =
            date -> {
                Timestamp timestamp = date.getSource();
                return timestamp == null? 0 : timestamp.getTime();
            };

//    private final Converter<Person, Integer> authorConverter =
//            author -> author.getSource().getId();

    public PostCommentMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(PostComment.class, CommentDTO.class)
                .addMappings(mapper -> mapper.using(parentConverter).map(PostComment::getParent, CommentDTO::setParentId))
                .addMappings(mapper -> mapper.using(timestampConverter).map(PostComment::getTime, CommentDTO::setTime));
//                .addMappings(mapper -> mapper.using(authorConverter).map(PostComment::getAuthor, CommentDTO::setAuthorId));
    }

    public CommentDTO mapToCommentDTO(PostComment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
