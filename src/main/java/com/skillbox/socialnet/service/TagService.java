package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import com.skillbox.socialnet.repository.Post2tagRepository;
import com.skillbox.socialnet.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final Post2tagRepository post2tagRepository;

    public void setPost2tags(Post post, List<Tag> tags) {
        List<Post2tag> post2tags = post2tagRepository.getAllByPost(post);
        List<Tag> existingTags = post2tags.stream().map(Post2tag::getTag).collect(Collectors.toList());
        existingTags.stream()
                .filter(tag -> !tags.contains(tag))
                .forEach(tag -> post2tagRepository.deleteByPostAndTag(post, tag));
        tags.stream()
                .filter(tag -> !existingTags.contains(tag))
                .forEach(tag -> createNewPost2tag(post, tag));
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

    public void createNewPost2tag(Post post, Tag tag) {
        Post2tag post2tag = new Post2tag(post, tag);
        post2tagRepository.save(post2tag);
    }

    public Tag createNewTag(String tagName) {
        Tag tag = new Tag();
        tag.setTagName(tagName);

        return tagRepository.save(tag);
    }

    public void addTags2Post(Post post, List<String> tagNames) {
        List<Tag> tags = addTagsIfNotExists(tagNames);
        setPost2tags(post, tags);
    }
}
