package com.brayck.library_api.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {

   @NotBlank(message = "Firstname cannot be empty")
   @NotEmpty(message = "Email cannot be empty")
   @Email(message = "Invalid email")
   private String email;

   @NotBlank(message = "Password cannot be empty")
   @NotEmpty(message = "Password cannot be empty")
   private String password;
}
