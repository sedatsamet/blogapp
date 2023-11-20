package com.sedat.bootcamp.blogapp.blogapp.repository;

import com.sedat.bootcamp.blogapp.blogapp.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findAllByOrderByDateDesc();
}
