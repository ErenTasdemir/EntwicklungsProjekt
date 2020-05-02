package com.github.entwicklungsprojekt.user.service;

import com.github.entwicklungsprojekt.exceptions.UserNotFoundException;
import com.github.entwicklungsprojekt.user.persistence.User;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findByUserId(id).orElseThrow(UserNotFoundException::new);
    }

    public User findByUsername(String username) {
        return userRepository.findByUserUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User saveUser(String userUsername,String userName, String userLastname, String userPassword) {
        User user = new User();
        user.setUserUsername(userUsername);
        user.setUserName(userName);
        user.setUserLastname(userLastname);
        user.setUserPassword(userPassword);
        userRepository.save(user);

        return user;
    }

    public User deleteUser(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);

        return user;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUserUsername(username);
    }
}
