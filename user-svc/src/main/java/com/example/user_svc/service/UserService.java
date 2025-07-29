package com.example.user_svc.service;

import com.example.user_svc.dto.UserDto;
import com.example.user_svc.dto.UserWithIdDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.exception.UserNotFoundException;
import com.example.user_svc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public UserWithIdDto getUserById(UUID id) {
        User user = this.userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with ID " + id + " not found"));
        return new UserWithIdDto(user.getUid(), user.getLastName(), user.getFirstName());
    }
}
