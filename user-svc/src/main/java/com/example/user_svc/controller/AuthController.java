package com.example.user_svc.controller;

import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.dto.LoginRequestDto;
import com.example.user_svc.dto.LoginResponseDto;
import com.example.user_svc.dto.UserDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.repository.UserRepository;
import com.example.user_svc.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserDto userDto) {
        User user = authService.createUser(userDto);
        UserDto response = new UserDto(user.getUsername(), user.getLastName(), user.getFirstName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
