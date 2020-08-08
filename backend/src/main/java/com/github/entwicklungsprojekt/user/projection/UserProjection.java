package com.github.entwicklungsprojekt.user.projection;

import com.github.entwicklungsprojekt.user.persistence.User;

/**
 * Projection of {@link User}s only containing data that is relevant to the client.
 */
public interface UserProjection {

    Long getUserId();

    String getUserUsername();

    String getUserName();

    String getUserLastname();

}
