package com.jaz.movie.booking.exception;

public class JwtExpiredException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public JwtExpiredException(String message) {
       super(message);
   }
}
