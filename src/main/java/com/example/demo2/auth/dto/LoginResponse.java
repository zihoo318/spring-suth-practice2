package com.example.demo2.auth.dto;

public record LoginResponse(String accessToken, String refreshToken, String logMessage) {}
