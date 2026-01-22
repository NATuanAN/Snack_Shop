package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.TestDataHelper;
import com.Snack_BE.config.UserMapper;
import com.Snack_BE.util.JwtUtil;

/**
 * Unit Tests for UserService
 * Tests authentication, registration, and OAuth flows
 * Uses Mockito for mocking dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private UserResponseDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // Setup test user entity
        testUser = new UserEntity();
        testUser.setUserID(TestDataHelper.TEST_USER_ID);
        testUser.setEmail(TestDataHelper.TEST_USER_EMAIL);
        testUser.setPassword("encodedPassword123");
        testUser.setName("Test User");

        // Setup test user DTO
        testUserDTO = new UserResponseDTO();
        testUserDTO.setEmail(TestDataHelper.TEST_USER_EMAIL);
        testUserDTO.setName("Test User");
    }

    // ==================== getAllUser Tests ====================

    @Test
    @DisplayName("getAllUser should return list of users when users exist")
    void getAllUser_ShouldReturnListOfUsers_WhenUsersExist() {
        // Arrange
        UserEntity user2 = new UserEntity();
        user2.setUserID(2L);
        user2.setEmail("user2@example.com");

        UserResponseDTO dto2 = new UserResponseDTO();
        dto2.setEmail("user2@example.com");
        dto2.setName("User 2");

        List<UserEntity> users = Arrays.asList(testUser, user2);

        when(userRepo.findAll()).thenReturn(users);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);
        when(userMapper.toDTO(user2)).thenReturn(dto2);

        // Act
        ResponseEntity<List<UserResponseDTO>> response = userService.getAllUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(TestDataHelper.TEST_USER_EMAIL, response.getBody().get(0).getEmail());
        verify(userRepo, times(1)).findAll();
        verify(userMapper, times(2)).toDTO(any(UserEntity.class));
    }

    // ==================== login Tests ====================

    @Test
    @DisplayName("login should return token and success when credentials are valid")
    void login_ShouldReturnTokenAndSuccess_WhenCredentialsValid() {
        // Arrange
        String email = TestDataHelper.TEST_USER_EMAIL;
        String password = TestDataHelper.TEST_USER_PASSWORD;
        String expectedToken = TestDataHelper.TEST_JWT_TOKEN;

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn(expectedToken);

        // Act
        ResponseEntity<Map<String, String>> response = userService.login(email, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login Successfully", response.getBody().get("message"));
        assertEquals(expectedToken, response.getBody().get("token"));
        
        verify(userRepo, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, testUser.getPassword());
        verify(jwtUtil, times(1)).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("login should return BAD_REQUEST when email or password is null")
    void login_ShouldReturnBadRequest_WhenEmailOrPasswordNull() {
        // Test with null email
        ResponseEntity<Map<String, String>> response1 = userService.login(null, "password");
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals("Both email and password are required", response1.getBody().get("message"));

        // Test with null password
        ResponseEntity<Map<String, String>> response2 = userService.login("email@test.com", null);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        assertEquals("Both email and password are required", response2.getBody().get("message"));

        // Test with both null
        ResponseEntity<Map<String, String>> response3 = userService.login(null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());

        // Verify no repository calls
        verify(userRepo, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("login should return NOT_FOUND when email does not exist")
    void login_ShouldReturnNotFound_WhenEmailNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Map<String, String>> response = userService.login(email, password);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Email is not valid", response.getBody().get("message"));
        
        verify(userRepo, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("login should return UNAUTHORIZED when password is wrong")
    void login_ShouldReturnUnauthorized_WhenPasswordWrong() {
        // Arrange
        String email = TestDataHelper.TEST_USER_EMAIL;
        String wrongPassword = "wrongPassword123";

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(wrongPassword, testUser.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> response = userService.login(email, wrongPassword);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The email or password are wrong", response.getBody().get("message"));
        
        verify(userRepo, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(wrongPassword, testUser.getPassword());
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), anyLong());
    }

    // ==================== register Tests ====================

    @Test
    @DisplayName("register should create user when email does not exist")
    void register_ShouldCreateUser_WhenEmailNotExist() {
        // Arrange
        String email = TestDataHelper.TEST_USER_EMAIL;
        String password = TestDataHelper.TEST_USER_PASSWORD;
        String name = "New User";
        String encodedPassword = "encodedPassword123";

        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // Act
        ResponseEntity<Map<String, String>> response = userService.register(email, password, name);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User is created successfully", response.getBody().get("message"));
        
        verify(userRepo, times(1)).existsByEmail(email);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("register should return CONFLICT when email already exists")
    void register_ShouldReturnConflict_WhenEmailAlreadyExists() {
        // Arrange
        String email = TestDataHelper.TEST_USER_EMAIL;
        String password = TestDataHelper.TEST_USER_PASSWORD;
        String name = "Test User";

        when(userRepo.existsByEmail(email)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> response = userService.register(email, password, name);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User have already existed", response.getBody().get("message"));
        
        verify(userRepo, times(1)).existsByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    // ==================== registerOAuthUser Tests ====================

    @Test
    @DisplayName("registerOAuthUser should return token when user exists")
    void registerOAuthUser_ShouldReturnToken_WhenUserExists() {
        // Arrange
        String email = TestDataHelper.TEST_OAUTH_EMAIL;
        String expectedToken = TestDataHelper.TEST_JWT_TOKEN;

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn(expectedToken);

        // Act
        String token = userService.registerOAuthUser(email);

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
        
        verify(userRepo, times(1)).findByEmail(email);
        verify(jwtUtil, times(1)).generateToken(anyString(), anyString(), anyLong());
    }
}
