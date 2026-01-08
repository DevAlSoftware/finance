package com.devalFinance.application.usecase.user;

import com.devalFinance.application.dto.request.LoginRequest;
import com.devalFinance.application.dto.response.AuthResponse;
import com.devalFinance.domain.model.User;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenerateTokenUseCase generateTokenUseCase;

    public AuthenticateUserUseCase(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   GenerateTokenUseCase generateTokenUseCase) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.generateTokenUseCase = generateTokenUseCase;
    }

    public AuthResponse execute(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());
        
        validatePassword(request.getPassword(), user.getPassword());
        validateUserIsActive(user);
        
        return generateTokenUseCase.execute(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos"));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }
    }

    private void validateUserIsActive(User user) {
        if (!user.isActive()) {
            throw new IllegalStateException("Su cuenta está inactiva. Contacte al soporte");
        }
    }
}

