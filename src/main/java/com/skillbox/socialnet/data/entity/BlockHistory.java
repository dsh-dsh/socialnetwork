package com.skillbox.socialnet.data.entity;

import com.skillbox.socialnet.data.enums.BlockHistoryAction;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "block_history")
public class BlockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "time")
    private Date time;
    @ManyToOne
    @JoinColumn(name = "fk_person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "fk_post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "fk_comment_id")
    private PostComment comment;
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private BlockHistoryAction action;

}
