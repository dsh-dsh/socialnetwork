package com.skillbox.socialnet.model.entity;

import com.skillbox.socialnet.model.enums.MessageReadStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "time")
    private Date time;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Person recipient;
    @Column(name = "message_text")
    private String messageText;
    @Column(name = "read_status")
    @Enumerated(EnumType.STRING)
    private MessageReadStatus readStatus;
}
