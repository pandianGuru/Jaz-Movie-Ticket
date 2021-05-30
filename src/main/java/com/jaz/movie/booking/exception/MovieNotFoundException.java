package com.jaz.movie.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

 public class MovieNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MovieNotFoundException(String message) {
		super(message);
	}
}
