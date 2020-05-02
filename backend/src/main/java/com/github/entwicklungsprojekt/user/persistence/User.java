package com.github.entwicklungsprojekt.user.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    @NotNull
    @Column(name = "user_username")
    String userUsername;

    @NotNull
    @Column(name = "user_name")
    String userName;

    @NotNull
    @Column(name = "user_lastname")
    String userLastname;

    @NotNull
    @Column(name = "user_password")
    String userPassword;


    public User(String userUsername, String userName, String userLastname, String userPassword) {
        this.userUsername = userUsername;
        this.userName = userName;
        this.userLastname = userLastname;
        this.userPassword = userPassword;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
