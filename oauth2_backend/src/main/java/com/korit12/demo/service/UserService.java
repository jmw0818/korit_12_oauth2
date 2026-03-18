package com.korit12.demo.service;

import com.korit12.demo.dto.AuthResponseDto;
import com.korit12.demo.dto.SignupRequestDto;
import com.korit12.demo.entity.User;
import com.korit12.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto signup(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 email입니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = User.createLocalUser(dto.getEmail(), encodedPassword, dto.getName());

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

    }
}
