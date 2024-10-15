package com.brayck.library_api.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode {
   NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
   INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
   NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),
   ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
   ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
   BAD_CREDENTIALS(400, HttpStatus.FORBIDDEN, "Login and / or Password is incorrect"),
   ;

   private final int code;
   private final String description;
   private final HttpStatus httpStatus;

   BusinessErrorCode(int code, HttpStatus status, String description) {
      this.code = code;
      this.description = description;
      this.httpStatus = status;
   }
}
