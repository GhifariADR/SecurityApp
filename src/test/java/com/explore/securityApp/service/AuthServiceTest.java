package com.explore.securityApp.service;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.auth.LoginRequest;
import com.explore.securityApp.dto.auth.RefreshTokenRequest;
import com.explore.securityApp.dto.auth.RegisterRequest;
import com.explore.securityApp.entity.RefreshToken;
import com.explore.securityApp.entity.User;
import com.explore.securityApp.exception.AlreadyExistException;
import com.explore.securityApp.exception.BadRequestException;
import com.explore.securityApp.exception.NotFoundException;
import com.explore.securityApp.exception.UnauthorizedException;
import com.explore.securityApp.repository.RefreshTokenRepository;
import com.explore.securityApp.repository.UserRepository;
import com.explore.securityApp.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginRequest request;
    private RefreshToken refreshToken;
    private RefreshTokenRequest refreshTokenRequest;
    private RegisterRequest registerRequest;



    @Nested
    class loginTest{
        @BeforeEach
        void setUp(){
            user = new User();
            user.setId(1L);
            user.setUsername("admin");
            user.setPassword("encodedPassword");
            user.setRole("ADMIN");

            request = new LoginRequest();
            request.setUsername("admin");
            request.setPassword("123456");
        }

        @Test
        void login_success() {

            when(userRepository.findByUsername("admin"))
                    .thenReturn(Optional.of(user));

            when(passwordEncoder.matches("123456", "encodedPassword"))
                    .thenReturn(true);

            when(jwtUtil.generateAccessToken("admin"))
                    .thenReturn("access-token");

            when(jwtUtil.generateRefreshToken())
                    .thenReturn("refresh-token");

            ApiResponse<?> response = authService.login(request);

            assertNotNull(response);
            assertEquals("Success", response.getMessage());

            verify(userRepository, times(1)).findByUsername("admin");
            verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
            verify(jwtUtil, times(1)).generateAccessToken("admin");
            verify(jwtUtil, times(1)).generateRefreshToken();
        }

        @Test
        void login_user_not_found() {

            when(userRepository.findByUsername("admin"))
                    .thenReturn(Optional.empty());

            assertThrows(UnauthorizedException.class,
                    () -> authService.login(request));

            verify(refreshTokenRepository, never()).save(any());
        }

        @Test
        void login_wrong_password() {

            when(userRepository.findByUsername("admin"))
                    .thenReturn(Optional.of(user));

            when(passwordEncoder.matches("123456", "encodedPassword"))
                    .thenReturn(false);

            assertThrows(UnauthorizedException.class,
                    () -> authService.login(request));

            verify(refreshTokenRepository, never()).save(any());
        }
    }

    @Nested
    class RefreshTokenTest{

        @BeforeEach
        void setUp(){

            user = new User();
            user.setId(1L);
            user.setUsername("admin");
            user.setPassword("encodedPassword");
            user.setRole("ADMIN");

            refreshTokenRequest = new RefreshTokenRequest();
            refreshTokenRequest.setRefreshToken("valid-refresh-token");

            refreshToken = new RefreshToken();
            refreshToken.setId(1L);
            refreshToken.setToken("valid-refresh-token");
            refreshToken.setRevoke(false);
            refreshToken.setExpiryDate(LocalDateTime.now().plusHours(1));
            refreshToken.setUser(user);
        }


        @Test
        void refresh_token_success () {

            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(1));

            when(refreshTokenRepository.findByToken("valid-refresh-token"))
                    .thenReturn(Optional.of(refreshToken));

            when(jwtUtil.generateAccessToken("admin"))
                    .thenReturn("new-access-token");

            ApiResponse<?> response = authService.refresh(refreshTokenRequest);

            assertNotNull(response);
            assertEquals("Success", response.getMessage());
            assertEquals("new-access-token", response.getData());

            verify(jwtUtil, times(1)).generateAccessToken("admin");
            verify(refreshTokenRepository, never()).delete(any());

        }

        @Test
        void refresh_token_not_found(){

            when(refreshTokenRepository.findByToken("valid-refresh-token"))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> authService.refresh(refreshTokenRequest));

        }

        @Test
        void refresh_token_expired() {
            refreshToken.setExpiryDate(LocalDateTime.now().minusDays(3));

            when(refreshTokenRepository.findByToken("valid-refresh-token"))
                    .thenReturn(Optional.of(refreshToken));

            assertThrows(BadRequestException.class, () -> authService.refresh(refreshTokenRequest));

            verify(refreshTokenRepository).delete(refreshToken);
        }

    }

    @Nested
    class RegisterTest {

        @BeforeEach
        void setup(){
            registerRequest = new RegisterRequest();
            registerRequest.setEmail("test@mail.com");
            registerRequest.setPassword("12345");
            registerRequest.setUsername("testing");
            registerRequest.setRole("USER");

        }

        @Test
        void register_success(){
            when(userRepository.findByUsername("testing"))
                    .thenReturn(Optional.empty());

            when(userRepository.findByEmail("test@mail.com"))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.encode("12345"))
                    .thenReturn("encodedPassword");

            ApiResponse<?> response = authService.register(registerRequest);

            assertNotNull(response);
            assertEquals("Success", response.getMessage());
            verify(userRepository).save(any(User.class));
        }

        @Test
        void register_username_already_exist(){
            when(userRepository.findByUsername("testing"))
                    .thenReturn(Optional.of(new User()));

            assertThrows(AlreadyExistException.class,() -> authService.register(registerRequest));
            verify(userRepository, never()).save(any());
        }

        @Test
        void register_email_already_exist(){
            when(userRepository.findByEmail("test@mail.com"))
                    .thenReturn(Optional.of(new User()));

            assertThrows(AlreadyExistException.class,() -> authService.register(registerRequest));
            verify(userRepository, never()).save(any());
        }
    }


}