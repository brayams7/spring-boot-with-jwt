package com.brayck.library_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

   @Value("${jwt.secret}")
   private String SECRET_KEY;

   @Value("${jwt.expiration}")
   private long EXPIRATION_TIME;

   @Value("${jwt.refresh-token.expiration}")
   private long REFRESH_EXPIRATION_TIME;

   public String extractUsername(String token){
      return extractClaim(token, Claims::getSubject);
   }

   public String generateToken(UserDetails userDetails){
      return generateToken(new HashMap<>(), userDetails);
   }

   public String generateToken(Map<String, Object> claims, UserDetails userDetails){
      return buildToken(claims, userDetails, EXPIRATION_TIME);
   }

   public String generateRefreshToken(UserDetails userDetails){
      return buildToken(new HashMap<>(), userDetails, REFRESH_EXPIRATION_TIME);
   }

   private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime){

      var authorities = userDetails.getAuthorities().stream()
              .map(authority -> authority.getAuthority())
              .toList();
      return Jwts
              .builder()
              .setClaims(extraClaims)
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date(System.currentTimeMillis())) // fecha de creacion del token
              .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) //24 horas
              .claim("authorities", authorities)
              .signWith(getSignInKey(), SignatureAlgorithm.HS256)
              .compact();
   }

   public boolean isTokenValid(String token, UserDetails userDetails){
      final String username = extractUsername(token);
      return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
   }

   private boolean isTokenExpired(String token){
      return extractExpiration(token).before(new Date());
   }

   private Date extractExpiration(String token){
      return extractClaim(token, Claims::getExpiration);
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   private Claims extractAllClaims(String token){
      return Jwts
              .parserBuilder()
              .setSigningKey(getSignInKey()) //firma digital del secreto
              .build()
              .parseClaimsJws(token)
              .getBody();
   }

   private Key getSignInKey(){
      // Decodifica la clave secreta de Base64 a un arreglo de bytes
      byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
      // Crea y retorna una clave HMAC-SHA
      return Keys.hmacShaKeyFor(keyBytes);
   }
}
