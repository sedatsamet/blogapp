package com.sedat.bootcamp.blogapp.blogapp.service;

import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            String extractedUser = extractUser(token);
            UUID userIdFromToken = getUserIdFromToken(extractedUser);
            User userFromDB = getUserFromDatabase(userIdFromToken);

            return userFromDB != null &&
                    isUserMatch(userFromDB, userDetails) &&
                    isTokenNotExpired(extractExpiration(token));
        } catch (Exception e) {
            throw new RuntimeException("Could not validate token");
        }
    }

    public String extractUser(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getSubject();
    }

    private boolean isUserMatch(User userFromDB, UserDetails userDetails) {
        return userFromDB.getId().equals(((User) userDetails).getId());
    }

    private boolean isTokenNotExpired(Date expirationDate) {
        Date currentDate = new Date();
        return expirationDate != null && expirationDate.after(currentDate);
    }

    private UUID getUserIdFromToken(String extractedUser) {
        try {
            return UUID.fromString(extractedUser);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private User getUserFromDatabase(UUID userId) {
        return userId != null ? userRepository.findById(userId).orElse(null) : null;
    }

    private Date extractExpiration(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getExpiration();
    }

    private Claims parseTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Map<String, Object> claims, String userName){
        User user = (User)userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getId().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch(Exception e) {
            throw new RuntimeException("Could not create token");
        }
    }
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
