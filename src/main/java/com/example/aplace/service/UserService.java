package com.example.aplace.service;

import com.example.aplace.dto.UserLoginDto;
import com.example.aplace.dto.UserResponseDto;
import com.example.aplace.dto.UserSignUpDto;
import com.example.aplace.entity.User;
import com.example.aplace.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signUp(UserSignUpDto signUpDto) {
        if (userRepository.existsByUsernameOrEmail(signUpDto.getUsername(), signUpDto.getEmail())) {
            throw new IllegalArgumentException("Username or Email already exists");
        }

        User user = User.builder()
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .role(User.UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);
        return convertToResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto login(UserLoginDto loginDto) {
        Optional<User> userOpt = userRepository.findByUsername(loginDto.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return convertToResponseDto(user);
    }

    private UserResponseDto convertToResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}