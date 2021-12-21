package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Set;

@Component
public class PostMapper {

    private final ModelMapper modelMapper;

    private final Converter<Timestamp, Long> timestampConverter =
            date -> {
                Timestamp timestamp = date.getSource();
                return timestamp == null? 0 : timestamp.getTime();
            };
    private final Converter<Set<Post2tag>, String[]> tagsConverter =
            (tags) -> tags.getSource().stream().map(Post2tag::getTag).map(Tag::getTag).toArray(String[]::new);

    public PostMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Post.class, PostDTO.class)
                .addMappings(m -> m.using(timestampConverter).map(Post::getTime, PostDTO::setTime))
                .addMappings(m -> m.using(tagsConverter).map(Post::getTags, PostDTO::setTags));

    }

    public PostDTO mapToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
