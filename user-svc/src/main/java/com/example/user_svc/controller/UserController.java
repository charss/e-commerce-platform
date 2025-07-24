package com.example.user_svc.controller;

import com.example.user_svc.dto.UserDto;
import com.example.user_svc.repository.UserRepository;
import com.example.user_svc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users =  userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
