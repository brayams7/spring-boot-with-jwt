package com.brayck.library_api.auth.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
public class RegisterRequest {

   @NotEmpty(message = "Firstname cannot be empty")
   @NotBlank(message = "Firstname cannot be empty")
   private String firstname;

   @NotBlank(message = "Firstname cannot be empty")
   @NotEmpty(message = "Lastname cannot be empty")
   private String lastname;

   @NotBlank(message = "Firstname cannot be empty")
   @NotEmpty(message = "Email cannot be empty")
   @Email(message = "Invalid email")
   private String email;

   @Size(min = 8, message = "Password must have at least 8 characters")
   @NotBlank(message = "Firstname cannot be empty")
   @NotEmpty(message = "Password cannot be empty")
   private String password;
}
