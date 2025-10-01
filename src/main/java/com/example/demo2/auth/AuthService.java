package com.example.demo2.auth;

import com.example.demo2.auth.dto.LoginRequest;
import com.example.demo2.auth.dto.LoginResponse;
import com.example.demo2.auth.dto.SignupRequest;
import com.example.demo2.security.JwtUtil;
import com.example.demo2.user.UserRepository;
import com.example.demo2.user.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity signUp(SignupRequest request){
        if (userRepository.findByUsername(request.userName()).isPresent()) {
            throw new RuntimeException("이미 생성된 유저입니다.");
        }

//        UserEntity user = new UserEntity();
//        user.setUsername(request.userName());
//        user.setPassword(passwordEncoder.encode(request.password()));
//        user.setRole(request.role());
//        UserEntity saved = userRepository.save(user); // DB 저장

        String encodedPw = passwordEncoder.encode(request.password()); // 비밀번호 인코딩

        UserEntity user = UserEntity.builder()
                .username(request.userName().trim())
                .password(encodedPw)        // 인코딩된 값만 엔티티에 넣기
                .role(request.role())
                .build();

        return userRepository.save(user);   // DB 저장
    }

    public LoginResponse login(LoginRequest request){
        UserEntity user = userRepository.findByUsername(request.userName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) { // 해시 비교 matches()
            throw new RuntimeException("Invalid password"); // 비번 틀린 경우
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(),user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return new LoginResponse(accessToken,refreshToken, null); // 로그인 성공 시 토큰 둘다 발급
    }

    public LoginResponse refreshAccessToken(String refreshToken, HttpServletResponse response){
        if (!jwtUtil.validateToken(refreshToken)) { // 유효하지 않은 경우
            // 브라우저의 refresh 쿠키 삭제
            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 만료 처리
            response.addCookie(cookie);

            throw new RuntimeException("Invalid refresh token"); // 글로벌 예외 처리로 401 반환 가능
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole()); // 토큰 새로 생성

        return new LoginResponse(newAccessToken, refreshToken, "refresh로 access 재발급");
    }

}
