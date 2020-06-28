package com.github.entwicklungsprojekt.user.persistence;

import com.github.entwicklungsprojekt.shop.persistence.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotEmpty
    @Column(name = "username")
    String username;

    @NotNull
    @NotEmpty
    @Column(name = "user_firstname")
    String userFirstname;

    @NotNull
    @NotEmpty
    @Column(name = "user_lastname")
    String userLastname;

    @NotNull
    @NotEmpty
    @Column(name = "user_password")
    String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Shop> shops;

    public User(@NotNull @NotEmpty String username, @NotNull @NotEmpty String userFirstname, @NotNull @NotEmpty String userLastname, @NotNull @NotEmpty String userPassword, List<String> roles) {
        this.username = username;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.password = userPassword;
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public void setUserPassword(String userPassword) {
        this.password = userPassword;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
