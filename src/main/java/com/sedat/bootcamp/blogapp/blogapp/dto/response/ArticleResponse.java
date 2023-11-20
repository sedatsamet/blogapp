package com.sedat.bootcamp.blogapp.blogapp.dto.response;

import com.sedat.bootcamp.blogapp.blogapp.entity.Comment;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
public record ArticleResponse(
        UUID id,
        String title,
        String content,
        String author,
        Date date,
        List<Comment> commentList
) {
}
