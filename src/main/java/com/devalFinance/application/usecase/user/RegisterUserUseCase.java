package com.devalFinance.application.usecase.user;

import com.devalFinance.application.dto.request.RegisterUserRequest;
import com.devalFinance.application.dto.response.AuthResponse;
import com.devalFinance.domain.model.PlanType;
import com.devalFinance.domain.model.Subscription;
import com.devalFinance.domain.model.SubscriptionStatus;
import com.devalFinance.domain.model.User;
import com.devalFinance.domain.repository.MembershipPlanRepository;
import com.devalFinance.domain.repository.SubscriptionRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RegisterUserUseCase {
    
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final ValidatePasswordStrengthUseCase validatePasswordStrengthUseCase;

    public RegisterUserUseCase(UserRepository userRepository,
                               MembershipPlanRepository membershipPlanRepository,
                               SubscriptionRepository subscriptionRepository,
                               PasswordEncoder passwordEncoder,
                               GenerateTokenUseCase generateTokenUseCase,
                               ValidatePasswordStrengthUseCase validatePasswordStrengthUseCase) {
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
        this.generateTokenUseCase = generateTokenUseCase;
        this.validatePasswordStrengthUseCase = validatePasswordStrengthUseCase;
    }

    @Transactional
    public AuthResponse execute(RegisterUserRequest request) {
        validateRequest(request);
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        User user = createUser(request);
        User savedUser = userRepository.save(user);
        
        createFreeSubscription(savedUser);
        
        return generateTokenUseCase.execute(savedUser);
    }

    private void validateRequest(RegisterUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        
        validatePasswordStrengthUseCase.execute(request.getPassword());
    }

    private User createUser(RegisterUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }

    private void createFreeSubscription(User user) {
        membershipPlanRepository.findByPlanType(PlanType.FREE)
                .ifPresent(freePlan -> {
                    Subscription subscription = new Subscription();
                    subscription.setUserId(user.getId());
                    subscription.setMembershipPlanId(freePlan.getId());
                    subscription.setStartDate(LocalDateTime.now());
                    subscription.setStatus(SubscriptionStatus.ACTIVE);
                    subscriptionRepository.save(subscription);
                    
                    user.setMembershipPlanId(freePlan.getId());
                    userRepository.save(user);
                });
    }
}

