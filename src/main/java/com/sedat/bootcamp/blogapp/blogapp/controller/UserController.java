package com.sedat.bootcamp.blogapp.blogapp.controller;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.AuthRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.request.UserCreateRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.UserCreatedResponse;
import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome(){
        return "Hello World! this is welcome page";
    }
    @PostMapping("/addNewUser")
    public User addUser(@RequestBody UserCreateRequest request){
        return userService.createUser(request);
    }
    @GetMapping("/users")
    public List<User> getUser(){
        return userService.findAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<UserCreatedResponse> userLogin(@RequestBody AuthRequest request){
        return userService.userLogin(request);
    }

    @GetMapping("/user")
    public String getUserString(){
        return "This is USER!";
    }

    @GetMapping("/admin")
    public String getAdminString(){
        return "This is ADMIN!";
    }
}
