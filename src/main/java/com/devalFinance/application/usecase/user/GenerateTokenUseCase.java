package com.devalFinance.application.usecase.user;

import com.devalFinance.application.dto.response.AuthResponse;
import com.devalFinance.application.dto.response.UserResponse;
import com.devalFinance.domain.model.User;
import com.devalFinance.infrastructure.security.JwtTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class GenerateTokenUseCase {
    
    private final JwtTokenProvider jwtTokenProvider;

    public GenerateTokenUseCase(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse execute(User user) {
        String userId = user.getId().toString();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail(), userId);
        
        UserResponse userResponse = mapToUserResponse(user);
        
        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getJwtExpiration() / 1000,
                userResponse
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getMembershipPlanId(),
                user.getCreatedAt(),
                user.getActive()
        );
    }
}

