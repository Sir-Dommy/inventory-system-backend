package com.inventory.App;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.inventory.app.entity.UserInfo;
import com.inventory.app.repository.UserInfoRepository;
import com.inventory.app.service.UserInfoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTests {

    @Mock
    private UserInfoRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserInfoService userInfoService;

    // In this test, we are testing for three things
    // 1. Check if service can find valid user using their username
    // 2. Check if service can flag invalid details using username
    // 3. Try to add a user and compare the output of test and that of the service

    @Test
    public void loadUserByUsername_WithValidUsername_ShouldReturnUserInfoDetails() {
        // Arrange
        String username = "user";
        UserInfo userInfo = new UserInfo(1, username, "password");
        when(repository.findByName(username)).thenReturn(Optional.of(userInfo));

        // Act
        UserDetails userDetails = userInfoService.loadUserByUsername(username);

        // Assert
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_WithInvalidUsername_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String username = "invaliduser";
        when(repository.findByName(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userInfoService.loadUserByUsername(username));
    }

    @Test
    public void addUser_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        UserInfo userInfo = new UserInfo(1, "user", "password");
        when(encoder.encode(userInfo.getPassword())).thenReturn("encodedPassword");

        // Act
        String result = userInfoService.addUser(userInfo);

        // Assert
        assertEquals("User: "+userInfo.getName()+" Added Successfully ", result);
        verify(encoder, times(1)).encode("password");
        verify(repository, times(1)).save(userInfo);
    }
}
