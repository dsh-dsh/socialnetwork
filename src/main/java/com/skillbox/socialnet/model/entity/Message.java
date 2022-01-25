package com.skillbox.socialnet.model.entity;

import com.skillbox.socialnet.model.enums.MessageReadStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"time"})
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "time")
    private LocalDateTime time;
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

    @ManyToOne
    private Dialog dialog;
}
