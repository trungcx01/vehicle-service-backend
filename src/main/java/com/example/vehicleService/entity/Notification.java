package com.example.vehicleService.entity;

import com.example.vehicleService.entity.enums.Event;
import com.example.vehicleService.entity.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message", nullable = false)
    private String message;

//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "event_type", nullable = false)
//    private Event eventType;
//
//    @Column(name = "event_id", nullable = false)
//    private Long eventId;

//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "notification_status", nullable = false)
//    private NotificationStatus notificationStatus;

    @ManyToMany
    @JoinTable(
            name = "notification_user",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
