package com.explore.securityApp.dto.auth;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank(message = "cannot be blank")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
