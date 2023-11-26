package com.sedat.bootcamp.blogapp.blogapp.dto.request;

public record CreateCommentRequest(
        String content,
        String articleId
) {
}
