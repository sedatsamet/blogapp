package com.sedat.bootcamp.blogapp.blogapp.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ArticleUpdateRequest(
        UUID articleId,
        String title,
        String content
) {
}
