package com.example.demo2.auth;

import com.example.demo2.auth.dto.LoginRequest;
import com.example.demo2.auth.dto.LoginResponse;
import com.example.demo2.auth.dto.SignupRequest;
import com.example.demo2.user.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private  final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> signUp(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse token = authService.login(request);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", token.refreshToken())
                .httpOnly(true).secure(false).path("/").sameSite("Lax")
                .maxAge(60 * 60 * 24 * 2)   // 2일
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new LoginResponse(token.accessToken(), null, "두 토큰 모두 재발급"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue("refreshToken") String refreshToken,
                                                 HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken, response)); // new 토큰 반환
    }
}
