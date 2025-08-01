package com.example.user_svc;

import com.example.user_svc.controller.AuthController;
import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.dto.LoginRequestDto;
import com.example.user_svc.dto.LoginResponseDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.exception.InvalidCredentialsException;
import com.example.user_svc.exception.UsernameAlreadyExistsException;
import com.example.user_svc.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDto request = new LoginRequestDto("user1", "password");
        UUID id = UUID.fromString("f7b727dd-9bc4-450d-b462-8da86255f4d2");
        LoginResponseDto response = new LoginResponseDto("mocked-jwt-token", id);

        when(authService.login(Mockito.any(LoginRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void shouldReturn401OnLoginFailed() throws Exception {
        LoginRequestDto request = new LoginRequestDto("user1", "password");

        when(authService.login(Mockito.any(LoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password."));

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        CreateUserDto userDto = new CreateUserDto("user2", "Doe", "Jane", "password123");
        User savedUser = new User();
        savedUser.setUsername("user2");
        savedUser.setFirstName("Jane");
        savedUser.setLastName("Doe");

        when(authService.createUser(Mockito.any(CreateUserDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user2"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldReturn409OnRegisterWhenUsernameExists() throws Exception {
        CreateUserDto userDto = new CreateUserDto("user2", "Doe", "Jane", "password123");
        User savedUser = new User();
        savedUser.setUsername("user2");
        savedUser.setFirstName("Jane");
        savedUser.setLastName("Doe");

        when(authService.createUser(Mockito.any(CreateUserDto.class)))
                .thenThrow(new UsernameAlreadyExistsException("Username '" + userDto.username() + "' already exists."));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
    }
}
