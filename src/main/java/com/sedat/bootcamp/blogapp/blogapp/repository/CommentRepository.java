package com.sedat.bootcamp.blogapp.blogapp.repository;

import com.sedat.bootcamp.blogapp.blogapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
