package com.github.entwicklungsprojekt.user.service;

import com.github.entwicklungsprojekt.exceptions.UserNotFoundException;
import com.github.entwicklungsprojekt.user.persistence.MyUserPrincipal;
import com.github.entwicklungsprojekt.user.persistence.User;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserUsername(username).orElseThrow(UserNotFoundException::new);

        return new MyUserPrincipal(user);
    }
}
