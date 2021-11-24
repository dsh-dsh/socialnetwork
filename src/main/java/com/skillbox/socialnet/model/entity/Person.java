package com.skillbox.socialnet.model.entity;

import com.skillbox.socialnet.model.enums.MessagesPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "reg_date")
    private Timestamp regData;
    @Column(name = "birth_date")
    private Timestamp birthDate;
    @Column(name = "e_mail", unique = true)
    private String eMail;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
    @Column(name = "photo")
    private String photo;
    @Column(name = "about")
    private String about;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "confirmation_code")
    private String confirmationCode;
    @Column(name = "is_approved")
    private boolean isApproved;
    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagesPermission;
    @Column(name = "last_online_time")
    private Timestamp lastOnlineTime;
    @Column(name = "is_blocked")
    private boolean isBlocked;
}
