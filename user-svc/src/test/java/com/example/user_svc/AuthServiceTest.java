package com.example.user_svc;

import com.example.shared.dto.CreateShoppingCartDto;
import com.example.user_svc.client.ShoppingCartSvcClient;
import com.example.user_svc.dto.CreateUserDto;
import com.example.user_svc.entity.User;
import com.example.user_svc.exception.CreateShoppingCartException;
import com.example.user_svc.repository.UserRepository;
import com.example.user_svc.service.AuthService;
import com.shared.feign.notification.NotificationClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    private ShoppingCartSvcClient shoppingCartSvcClient;
    @Mock
    private NotificationClient notifClient;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserDto dto = new CreateUserDto("user1", "password", "Jane", "Doe");

        when(userRepo.existsByUsername("user1")).thenReturn(false);
        doNothing().when(notifClient).sendEmail(any());;

        User savedUser = new User();
        savedUser.setUsername("user1");
        savedUser.setFirstName("Jane");
        savedUser.setLastName("Doe");
        savedUser.setPassword("password");

        when(userRepo.saveAndFlush(any(User.class))).thenReturn(savedUser);
        User result = authService.createUser(dto);

        assertEquals("user1", result.getUsername());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("password", result.getPassword());

        verify(shoppingCartSvcClient, times(1))
                .createShoppingCart(new CreateShoppingCartDto(savedUser.getUid()));

        verify(userRepo).existsByUsername("user1");
        verify(userRepo).saveAndFlush(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenShoppingCartCreationFailed() throws Exception {
        CreateUserDto userDto = new CreateUserDto("testuser", "Doe", "John", "password123");
        when(userRepo.existsByUsername("testuser")).thenReturn(false);

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setPassword("password");

        when(userRepo.saveAndFlush(any(User.class))).thenReturn(savedUser);
        doThrow(new CreateShoppingCartException("Shopping cart service down"))
                .when(shoppingCartSvcClient)
                .createShoppingCart(any(CreateShoppingCartDto.class));

        assertThrows(RuntimeException.class, () -> authService.createUser(userDto));

        verify(shoppingCartSvcClient, times(1))
                .createShoppingCart(new CreateShoppingCartDto(savedUser.getUid()));

    }
}
