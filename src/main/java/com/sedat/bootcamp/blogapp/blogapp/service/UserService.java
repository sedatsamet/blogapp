package com.sedat.bootcamp.blogapp.blogapp.service;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.AuthRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.request.UserCreateRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.UserCreatedResponse;
import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public UserService(UserRepository userRepository,
                       @Lazy BCryptPasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public ResponseEntity<UserCreatedResponse> userLogin(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if(authentication.isAuthenticated()){
            String token = jwtService.generateToken(request.username());
            UserCreatedResponse userCreatedResponse = UserCreatedResponse.builder()
                    .authToken(token)
                    .build();
            return ResponseEntity.ok(userCreatedResponse);
        }
        throw new UsernameNotFoundException("Invalid Username " + request.username());
    }

    public User createUser(UserCreateRequest request){
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .telephone(request.telephone())
                .authorities(request.authorities())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .build();
        return userRepository.save(newUser);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
