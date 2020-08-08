package com.github.entwicklungsprojekt.user.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.entwicklungsprojekt.user.persistence.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object that holds {@link User} information that gets received from the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPayload {

    String username;

    String name;

    String lastname;

    String password;

}
