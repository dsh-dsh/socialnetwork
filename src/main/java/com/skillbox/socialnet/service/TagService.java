package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import com.skillbox.socialnet.repository.Tag2PostRepository;
import com.skillbox.socialnet.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;

    public Set<Post2tag> getPost2tagSet(Post post, List<Tag> tags) {
        return tags.stream()
                .map(tag -> new Post2tag(post, tag))
                .collect(Collectors.toSet());
    }

    public List<Tag> addTagsIfNotExists(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> createNewTag(tagName));
            tags.add(tag);
        }

        return tags;
    }

    public Tag createNewTag(String tagName) {
        Tag tag = new Tag();
        tag.setTagName(tagName);

        return tagRepository.save(tag);
    }

}
