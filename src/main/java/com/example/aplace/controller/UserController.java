package com.example.aplace.controller;

import com.example.aplace.dto.UserLoginDto;
import com.example.aplace.dto.UserResponseDto;
import com.example.aplace.dto.UserSignUpDto;
import com.example.aplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private  UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignUpDto signUpDto) {
        UserResponseDto responseDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        UserResponseDto responseDto = userService.login(loginDto);
        return ResponseEntity.ok(responseDto);
    }
}