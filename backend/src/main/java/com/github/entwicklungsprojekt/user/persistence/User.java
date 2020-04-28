package com.github.entwicklungsprojekt.user.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "user")
@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @NotNull
    @Column(name = "user_id")
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
