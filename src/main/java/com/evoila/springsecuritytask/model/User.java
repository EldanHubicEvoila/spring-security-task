package com.evoila.springsecuritytask.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String email;

    @Column(length = 64)
    private String password;


    public User(String username,
                String email,
                String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
