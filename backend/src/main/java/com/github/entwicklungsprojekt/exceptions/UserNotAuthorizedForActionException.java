package com.github.entwicklungsprojekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Nutzer darf diesen Shop nicht bearbeiten.")
public class UserNotAuthorizedForActionException extends RuntimeException {
}
