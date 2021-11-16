package com.skillbox.socialnet.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "time")
    private LocalDateTime time;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Person author;
    @Column(name = "comment_text")
    private String commentText;
    @Column(name = "is_blocked")
    private boolean isBlocked;
}
