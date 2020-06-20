package com.github.entwicklungsprojekt.security;

import com.github.entwicklungsprojekt.exceptions.UserNotFoundException;
import com.github.entwicklungsprojekt.exceptions.UsernameAlreadyTakenException;
import com.github.entwicklungsprojekt.security.jwt.AuthenticationRequest;
import com.github.entwicklungsprojekt.security.jwt.JwtTokenProvider;
import com.github.entwicklungsprojekt.user.payload.UserPayload;
import com.github.entwicklungsprojekt.user.persistence.User;
import com.github.entwicklungsprojekt.user.persistence.UserRepository;
import com.github.entwicklungsprojekt.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthenticationRequest data) {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            User user = this.userService.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            model.put("token_expiration_date", token.)
            model.put("name", user.getUserFirstname());
            model.put("lastname", user.getUserLastname());
            model.put("id", user.getId());
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserPayload payload) {
        if (userService.existsByUsername(payload.getUsername())){
            throw new UsernameAlreadyTakenException();
        }
        User user = userService.saveUser(payload.getUsername(),payload.getName(), payload.getLastname(), passwordEncoder.encode(payload.getPassword()));
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        Map<Object, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("token", token);
        model.put("name", user.getUserFirstname());
        model.put("lastname", user.getUserLastname());
        model.put("id", user.getId());
        return ResponseEntity.ok(model);
    }
}
