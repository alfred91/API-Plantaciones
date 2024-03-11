package com.example.apijwt.dto;

import java.util.List;

public record LoginResponse(String username, List<String> authorities, String token) {
}
