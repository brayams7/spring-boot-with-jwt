package com.brayck.library_api.auth;

import com.brayck.library_api.auth.DTO.AuthenticationResponse;
import com.brayck.library_api.auth.DTO.LoginRequest;
import com.brayck.library_api.auth.DTO.RegisterRequest;
import com.brayck.library_api.email.EmailService;
import com.brayck.library_api.email.EmailTemplateName;
import com.brayck.library_api.role.RoleRepository;
import com.brayck.library_api.security.JwtService;
import com.brayck.library_api.token.Token;
import com.brayck.library_api.token.TokenRepository;
import com.brayck.library_api.user.User;
import com.brayck.library_api.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

   private final PasswordEncoder passwordEncoder;
   private final UserRepository userRepository;
   private final JwtService jwtService;
   private final RoleRepository roleRepository;
   private final TokenRepository tokenRepository;
   private final EmailService emailService;
   private final AuthenticationManager authenticationManager;

   @Value("${mailing.frontend.activation-url")
   private String activationUrl;

   public void register(RegisterRequest registerRequest) throws MessagingException {
      var userRole = roleRepository.findByName("USER")
              //->todo -> better exception handler
              .orElseThrow(()-> new IllegalArgumentException("ROLE USER was not initialized"));

      User user = User.builder()
              .firstname(registerRequest.getFirstname())
              .lastname(registerRequest.getLastname())
              .email(registerRequest.getEmail())
              .roles(List.of(userRole))
              .accountLocked(false)
              .enabled(false)
              .password(passwordEncoder.encode(registerRequest.getPassword()))
              .build();

      User savedUser = userRepository.save(user);
      sendValidationEmail(savedUser);

   }

   public AuthenticationResponse login(LoginRequest loginRequest) {

      var auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      loginRequest.getEmail(),
                      loginRequest.getPassword()
              )
      );


      var claims = new HashMap<String, Object>();
      var user = ((User) auth.getPrincipal());
      claims.put("fullName", user.getFullName());
      var token = jwtService.generateToken(claims, user);

      return AuthenticationResponse.builder().accessToken(token).build();
   }

   private void sendValidationEmail(User user) throws MessagingException {
      var newToken = generateAndSaveActivationToken(user);

      //todo -> send email
      emailService.sendEmail(
              user.getEmail(),
              user.getFullName(),
              "Activate your account",
              EmailTemplateName.ACTIVATE_ACCOUNT,
              activationUrl,
              newToken
      );

   }

   private String generateAndSaveActivationToken(User user) {
      String generatedToken = generateActivationCode(6);
      var token = Token.builder()
              .token(generatedToken)
              .createdAt(LocalDateTime.now())
              .expiresAt(LocalDateTime.now().plusMinutes(15))
              .user(user)
              .build();

      tokenRepository.save(token);

      return generatedToken;
   }

   private String generateActivationCode(int length) {
      String characters = "0123456789";

      StringBuilder sb = new StringBuilder(length);
      SecureRandom random = new SecureRandom(); // genera de forma segura un numero aleatorio
      for (int i = 0; i < length; i++) {
//         int index = (int) (Math.random() * characters.length());
         int index = random.nextInt(characters.length());
         sb.append(characters.charAt(index));
      }

      return sb.toString();
   }


//   @Transactional
   public void activateAccount(String token) throws MessagingException {

      var activationToken = tokenRepository.findByToken(token)
              .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
      if(LocalDateTime.now().isAfter(activationToken.getExpiresAt())){
         sendValidationEmail(activationToken.getUser());
         throw new RuntimeException("Activation token has expired. A new one has been sent to your email");
      }

      var user = userRepository.findById(activationToken.getUser().getId())
              .orElseThrow(() -> new IllegalArgumentException("User not found"));

      user.setEnabled(true);

      userRepository.save(user);

      activationToken.setValidatedAt(LocalDateTime.now());
      tokenRepository.save(activationToken);
   }
}
