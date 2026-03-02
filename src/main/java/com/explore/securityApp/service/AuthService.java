package com.explore.securityApp.service;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.auth.LoginRequest;
import com.explore.securityApp.dto.auth.LoginResponse;
import com.explore.securityApp.dto.auth.RefreshTokenRequest;
import com.explore.securityApp.entity.RefreshToken;
import com.explore.securityApp.entity.User;
import com.explore.securityApp.exception.BadRequestException;
import com.explore.securityApp.exception.NotFoundException;
import com.explore.securityApp.exception.UnauthorizedException;
import com.explore.securityApp.repository.RefreshTokenRepository;
import com.explore.securityApp.repository.UserRepository;
import com.explore.securityApp.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    public ApiResponse<?> login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid Credential"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UnauthorizedException("Invalid Credential");
        }

        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken();

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusDays(30));
        tokenEntity.setRevoke(false);
        tokenEntity.setUser(user);

        refreshTokenRepository.save(tokenEntity);

        LoginResponse response = new LoginResponse(accessToken,refreshToken);

        return ApiResponse.success("Success", response);
    }

    public ApiResponse<?> refresh(RefreshTokenRequest request){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new NotFoundException("Refresh token not Found"));

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expired");
        }

        String username = refreshToken.getUser().getUsername();

        String newAccessToken = jwtUtil.generateAccessToken(username);

        return ApiResponse.success("Success", newAccessToken);

    }
}
