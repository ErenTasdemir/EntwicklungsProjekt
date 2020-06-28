package com.github.entwicklungsprojekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Es ist schon ein Nutzer unter diesem Benutzernamen registriert!")
public class UsernameAlreadyTakenException extends RuntimeException{
}
