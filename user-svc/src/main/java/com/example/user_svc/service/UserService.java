package com.example.user_svc.service;

import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.dto.UserDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public List<UserDto> getAllUsers() {
        return this.userRepo.findAll().stream()
                .map(user -> new UserDto(
                        user.getUsername(),
                        user.getLastName(),
                        user.getFirstName())
                )
                .toList();
    }
}
