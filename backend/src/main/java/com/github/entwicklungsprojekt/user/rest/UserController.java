package com.github.entwicklungsprojekt.user.rest;

import com.github.entwicklungsprojekt.user.payload.UserPayload;
import com.github.entwicklungsprojekt.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id")String id) {
        return ResponseEntity.ok(userService.getUserById(Long.parseLong(id)));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserPayload payload) {

        return ResponseEntity.ok(userService.saveUser(payload.getUserUsername(),payload.getUserFirstname(), payload.getUserLastname(), passwordEncoder.encode(payload.getUserPassword())));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id")String id) {
        return ResponseEntity.ok(userService.deleteUser(Long.parseLong(id)));
    }

    @GetMapping("/currentuser")
    public ResponseEntity<?> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map( o -> ((GrantedAuthority) o).getAuthority())
                .collect(toList())
        );
        return ResponseEntity.ok(model);
    }

}
