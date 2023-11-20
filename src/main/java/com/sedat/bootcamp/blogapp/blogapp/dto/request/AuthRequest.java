package com.sedat.bootcamp.blogapp.blogapp.dto.request;

public record AuthRequest(
        String username,
        String password
) {
}
