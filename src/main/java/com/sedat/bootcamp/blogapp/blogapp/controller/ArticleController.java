package com.sedat.bootcamp.blogapp.blogapp.controller;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.ArticleResponse;
import com.sedat.bootcamp.blogapp.blogapp.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/getAllArticles")
    public ResponseEntity<List<ArticleResponse>> getAll(){
        return ResponseEntity.ok(articleService.getAll());
    }

    @PostMapping("/createArticle")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest request){
        return ResponseEntity.ok(articleService.createArticle(request));
    }
}
