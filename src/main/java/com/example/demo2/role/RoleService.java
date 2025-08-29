package com.example.demo2.role;

import com.example.demo2.role.dto.RoleResponse;
import com.example.demo2.security.JwtUtil;
import com.example.demo2.user.UserRepository;
import com.example.demo2.user.dto.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RoleService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public RoleResponse process(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return new RoleResponse(user.getUsername(), user.getRole());
    }
}
