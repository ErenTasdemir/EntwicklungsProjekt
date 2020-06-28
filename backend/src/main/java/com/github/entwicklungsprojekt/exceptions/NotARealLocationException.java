package com.github.entwicklungsprojekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Der angegebene Ort existiert nicht!")
public class NotARealLocationException extends RuntimeException {
}
