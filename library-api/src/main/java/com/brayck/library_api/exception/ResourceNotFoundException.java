package com.brayck.library_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceNotFoundException extends RuntimeException{

   private static final long SerialVersionUID = 1L;

   public ResourceNotFoundException(String message) {
      super(message);
   }
}
