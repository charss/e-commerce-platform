package com.example.user_svc;

import com.example.shared.dto.UserDto;
import com.example.shared.dto.UserWithIdDto;
import com.example.shared.exception.UserNotFoundException;
import com.example.user_svc.controller.UserController;
import com.example.user_svc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> mockUsers = List.of(
                new UserDto("chrlskyl", "Reyes", "Charles"),
                new UserDto("ynnhlcr", "Lucero", "Yannah"),
                new UserDto("testUser", "Test", "User")
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/api/v1/user"))  // change path if needed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].username", is("chrlskyl")))
                .andExpect(jsonPath("$[0].lastName", is("Reyes")))
                .andExpect(jsonPath("$[0].firstName", is("Charles")))
                .andExpect(jsonPath("$[1].username", is("ynnhlcr")))
                .andExpect(jsonPath("$[1].lastName", is("Lucero")))
                .andExpect(jsonPath("$[1].firstName", is("Yannah")))
                .andExpect(jsonPath("$[2].username", is("testUser")))
                .andExpect(jsonPath("$[2].lastName", is("Test")))
                .andExpect(jsonPath("$[2].firstName", is("User")));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UUID id = UUID.fromString("f7b727dd-9bc4-450d-b462-8da86255f4d2");

        UserWithIdDto mockUser = new UserWithIdDto(id, "Reyes", "Charles");

        when(userService.getUserById(id)).thenReturn(mockUser);

        mockMvc.perform(get("/api/v1/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.lastName").value("Reyes"))
                .andExpect(jsonPath("$.firstName").value("Charles"));

    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.getUserById(id))
                .thenThrow(new UserNotFoundException("User with ID " + id + " not found"));

        mockMvc.perform(get("/api/v1/user/" + id))
                .andExpect(status().isNotFound());
    }
}
