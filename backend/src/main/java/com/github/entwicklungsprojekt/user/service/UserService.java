package com.github.entwicklungsprojekt.user.service;

import com.github.entwicklungsprojekt.user.persistence.User;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

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
        User user = userRepository.getOne(userId);
        userRepository.delete(user);

        return user;
    }
}
