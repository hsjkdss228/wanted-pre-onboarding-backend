package com.inu.wanted.preassignment.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @EmbeddedId
    private UserId id;

    @Embedded
    private UserName name;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private User() {

    }

    public User(String id, String name) {
        this.id = UserId.of(id);
        this.name = UserName.of(name);
    }

    public UserId id() {
        return id;
    }
}
