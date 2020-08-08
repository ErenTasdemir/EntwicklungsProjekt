package com.github.entwicklungsprojekt.user.service;

import com.github.entwicklungsprojekt.exceptions.UserNotFoundException;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of interface {@link UserDetailsService}.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Instantiates a new My user details service.
     *
     * @param userRepository the user repository
     */
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
