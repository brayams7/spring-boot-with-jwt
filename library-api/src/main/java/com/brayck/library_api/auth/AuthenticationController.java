package com.brayck.library_api.auth;

import com.brayck.library_api.auth.DTO.AuthenticationResponse;
import com.brayck.library_api.auth.DTO.LoginRequest;
import com.brayck.library_api.auth.DTO.RegisterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthenticationController {

   private final AuthenticationService authenticationService;

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
      try {
         authenticationService.register(registerRequest);
      } catch (MessagingException e) {
         return ResponseEntity.internalServerError().body("Error sending email");
      }
      return ResponseEntity.ok("User registered successfully");
   }


   @PostMapping("/login")
   public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
      return ResponseEntity.ok(authenticationService.login(loginRequest));
   }
//
//   @PostMapping("/refresh-token")
//   public void  refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//      authenticationService.refreshToken(request, response);
//   }

   @GetMapping("/activate-account")
   public ResponseEntity<String> confirmAccount(@RequestParam(name = "token") String token) throws MessagingException {
      authenticationService.activateAccount(token);
      return ResponseEntity.ok("Account activated successfully");
   }

//   @GetMapping("/user/{username}")
//   public void getUser(@PathVariable("username") String username) {
//
//   }
}
