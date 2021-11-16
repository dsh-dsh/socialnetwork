package com.skillbox.socialnet.model.entity;

import com.skillbox.socialnet.model.enums.BlockHistoryAction;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "block_history")
public class BlockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "time")
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment comment;
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private BlockHistoryAction action;

}
