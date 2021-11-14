package com.skillbox.socialnet.data.entity;

import com.skillbox.socialnet.data.enums.MessagesPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "reg_data")
    private Date regData;
    @Column(name = "birth_date")
    private Date birthDate;
    @Column(name = "e_mail")
    private String eMail;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
    @Column(name = "photo")
    private String photo;
    @Column(name = "about")
    private String about;
    @Column(name = "town")
    private String town;
    @Column(name = "confirmation_code")
    private String confirmationCode;
    @Column(name = "is_approved")
    private boolean isApproved;
    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagesPermission;
    @Column(name = "last_online_time")
    private Date lastOnlineTime;
    @Column(name = "is_blocked")
    private boolean isBlocked;
}
