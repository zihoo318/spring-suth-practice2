package com.example.demo2.security.dto;

public record ErrorResponse(
        int status,
        String code,
        String message
) {}
