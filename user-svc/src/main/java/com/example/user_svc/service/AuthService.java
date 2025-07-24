package com.example.user_svc.service;

import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.dto.LoginRequestDto;
import com.example.user_svc.dto.LoginResponseDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.exception.InvalidCredentialsException;
import com.example.user_svc.exception.UsernameAlreadyExistsException;
import com.example.user_svc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepo.findByUsername(requestDto.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password."));

        if (!user.getPassword().equals(requestDto.password())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        return new LoginResponseDto(
                "JWT TOKEN HERE",
                user.getUid()
        );
    }

    public User createUser(CreateUserDto userDto) {

        if (userRepo.existsByUsername(userDto.username())) {
            throw new UsernameAlreadyExistsException("Username '" + userDto.username() + "' already exists.");
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPassword(userDto.password());

        return userRepo.save(user);
    }
}
