package com.github.entwicklungsprojekt.user.service;

import com.github.entwicklungsprojekt.exceptions.UserNotFoundException;
import com.github.entwicklungsprojekt.user.persistence.User;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User saveUser(String userUsername,String userFirstname, String userLastname, String userPassword) {
        userRepository.save(User.builder()
        .username(userUsername)
        .password(userPassword)
        .roles(Arrays.asList("ROLE_USER"))
        .userFirstname(userFirstname)
        .userLastname(userLastname)
        .build());

        return userRepository.findByUsername(userUsername).orElseThrow(UserNotFoundException::new);
    }

    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);

        return user;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
