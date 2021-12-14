package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PostModelMapper {

    private final ModelMapper modelMapper;

    private final Converter<Timestamp, Long> timestampConverter =
            date -> {
                Timestamp timestamp = date.getSource();
                return timestamp == null? 0 : timestamp.getTime();
            };

    public PostModelMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Post.class, PostDTO.class)
                .addMappings(m -> m.using(timestampConverter).map(Post::getTime, PostDTO::setTime));

    }

    public PostDTO mapToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
