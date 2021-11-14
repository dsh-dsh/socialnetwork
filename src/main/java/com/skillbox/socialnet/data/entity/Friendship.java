package com.skillbox.socialnet.data.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private FriendshipStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_person_id")
    private Person srcPerson;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dst_person_id")
    private Person dstPerson;
}
