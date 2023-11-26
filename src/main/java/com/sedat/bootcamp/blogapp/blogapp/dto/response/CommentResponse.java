package com.sedat.bootcamp.blogapp.blogapp.dto.response;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record CommentResponse(
        UUID commentId,
        String content,
        String author,
        Date date
) {
}
