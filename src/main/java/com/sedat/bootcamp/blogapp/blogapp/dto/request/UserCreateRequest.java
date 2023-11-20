package com.sedat.bootcamp.blogapp.blogapp.dto.request;

import com.sedat.bootcamp.blogapp.blogapp.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserCreateRequest(
        String name,
        String username,
        String password,
        String telephone,
        Set<Role> authorities
) {
}
