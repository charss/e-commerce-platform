package com.example.user_svc.service;

import com.example.shared.dto.CreateNotificationDto;
import com.example.shared.dto.CreateShoppingCartDto;
import com.example.shared.enums.NotificationType;
import com.example.user_svc.client.ShoppingCartSvcClient;
import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.dto.LoginRequestDto;
import com.example.user_svc.dto.LoginResponseDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.exception.InvalidCredentialsException;
import com.example.user_svc.exception.UsernameAlreadyExistsException;
import com.example.user_svc.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.feign.notification.NotificationClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final ShoppingCartSvcClient shoppingCartSvcClient;
    private final NotificationClient notifClient;

    public AuthService(UserRepository userRepo,
                       ShoppingCartSvcClient shoppingCartSvcClient, NotificationClient notifClient) {
        this.userRepo = userRepo;
        this.shoppingCartSvcClient = shoppingCartSvcClient;
        this.notifClient = notifClient;
    }


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

    @Transactional
    public User createUser(CreateUserDto userDto) {
        if (userRepo.existsByUsername(userDto.username())) {
            throw new UsernameAlreadyExistsException("Username '" + userDto.username() + "' already exists.");
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPassword(userDto.password());
        User saveUser = userRepo.saveAndFlush(user);

        shoppingCartSvcClient.createShoppingCart(new CreateShoppingCartDto(saveUser.getUid()));

        Map<String, Object> data = Map.of(
                "email", "ecomtestmail@yopmail.com",
                "firstName", user.getFirstName(),
                "lastName", user.getLastName()
        );
        JsonNode node = new ObjectMapper().valueToTree(data);
        notifClient.sendEmail(new CreateNotificationDto(
                NotificationType.USER_REGISTERED,
                user.getUid(),
                node
        ));
        return saveUser;
    }
}
