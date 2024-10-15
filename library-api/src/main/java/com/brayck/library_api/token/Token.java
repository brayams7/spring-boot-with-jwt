package com.brayck.library_api.token;


import com.brayck.library_api.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
@EntityListeners(AuditingEntityListener.class)
public class Token {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   private String token;

   private LocalDateTime createdAt;
   private LocalDateTime expiresAt;
   private LocalDateTime validatedAt;

   @ManyToOne
   @JoinColumn(name = "userId", nullable = false)
   private User user;

}
