package com.sparta.igeomubwotna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatusEnum status;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime statusModifiedAt;


    // @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    // private List<Recipe> recipes = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    // private List<Comment> comments = new ArrayList<>();


    // 생성자
    public User(String userId, String password, String name, String email, String description) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.description = description;
        this.status = UserStatusEnum.ACTIVE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}