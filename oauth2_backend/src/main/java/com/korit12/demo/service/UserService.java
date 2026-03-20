package com.korit12.demo.service;

import com.korit12.demo.dto.AuthResponseDto;
import com.korit12.demo.dto.LoginRequestDto;
import com.korit12.demo.dto.SignupRequestDto;
import com.korit12.demo.dto.UpdateUserRequestDto;
import com.korit12.demo.entity.User;
import com.korit12.demo.repository.UserRepository;
import com.korit12.demo.security.SecurityUtil;
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
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponseDto.of(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("email 혹은 비밀번호가 잘못되었습니다."));

        if (user.getPassword() == null) {
            throw new IllegalArgumentException("소셜 로그인 계정입니다. 구글/네이버/카카오 로그인을 이용해주세요.");
        }

        if (!passwordEncoder.matches(dto.getPassword(),user.getPassword())) {
            throw new IllegalArgumentException("email 또는 비밀번호가 잘못되었습니다.");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponseDto.of(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    public void verifyPassword(String password) {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("소셜 로그인 계정입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public AuthResponseDto updateUser(UpdateUserRequestDto dto) {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이름 수정
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.updateName(dto.getName());
        }

        // 비밀번호 수정 (입력했을 때만)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            user.updatePassword(encodedPassword);
        }

        return AuthResponseDto.of(
                jwtService.generateToken(user.getEmail(), user.getRole().name()),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }

}
