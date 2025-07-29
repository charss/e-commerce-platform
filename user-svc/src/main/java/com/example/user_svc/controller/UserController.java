package com.example.user_svc.controller;

import com.example.user_svc.dto.UserDto;
import com.example.user_svc.dto.UserWithIdDto;
import com.example.user_svc.repository.UserRepository;
import com.example.user_svc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWithIdDto> getUserById(@PathVariable(value = "id") UUID id) {
        UserWithIdDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
