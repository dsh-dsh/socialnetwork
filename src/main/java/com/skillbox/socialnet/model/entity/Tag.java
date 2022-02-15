package com.skillbox.socialnet.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"tagName"})
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "tag")
    private String tagName;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private Set<Post2tag> posts = new HashSet<>();

}
