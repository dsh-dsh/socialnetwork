package com.skillbox.socialnet.model.entity;

import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "notification_type_code")
    @Enumerated(EnumType.STRING)
    private NotificationTypeCode notificationTypeCode;

    private boolean permission;

    public NotificationSetting(Person person, NotificationTypeCode notificationTypeCode, boolean permission) {
        this.person = person;
        this.notificationTypeCode = notificationTypeCode;
        this.permission = permission;
    }
}
