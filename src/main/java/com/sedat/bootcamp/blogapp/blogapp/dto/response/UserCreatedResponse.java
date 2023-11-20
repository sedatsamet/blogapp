package com.sedat.bootcamp.blogapp.blogapp.dto.response;

import lombok.Builder;

@Builder
public record UserCreatedResponse(
        String authToken
) {
}
