package com.github.entwicklungsprojekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Benutzer existiert nicht!")
public class UserNotFoundException extends RuntimeException {

}
