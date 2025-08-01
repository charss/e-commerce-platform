package com.example.user_svc;

import com.example.shared.dto.UserDto;
import com.example.shared.dto.UserWithIdDto;
import com.example.shared.exception.UserNotFoundException;
import com.example.user_svc.entity.User;
import com.example.user_svc.repository.UserRepository;
import com.example.user_svc.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        List<User> mockUsers = List.of(
                new User(UUID.randomUUID(), "user1", "pass", "Doe", "John"),
                new User(UUID.randomUUID(), "user2", "pass", "Smith", "Anna")
        );
        when(userRepo.findAll()).thenReturn(mockUsers);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).username());
        assertEquals("Smith", result.get(1).lastName());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void shouldReturnUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "user3", "secret", "Reyes", "Charles");
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        UserWithIdDto result = userService.getUserById(userId);

        assertEquals(userId, result.id());
        assertEquals("Charles", result.firstName());
        verify(userRepo, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepo, times(1)).findById(userId);
    }
}
