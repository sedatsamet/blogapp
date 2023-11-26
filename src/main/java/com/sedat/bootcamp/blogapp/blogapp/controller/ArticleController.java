package com.sedat.bootcamp.blogapp.blogapp.controller;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleUpdateRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.ArticleResponse;
import com.sedat.bootcamp.blogapp.blogapp.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable String id) {
        ArticleResponse articleResponse = articleService.findById(UUID.fromString(id));
        return articleResponse != null ? ResponseEntity.ok(articleResponse) : ResponseEntity.notFound().build();
    }

    @PutMapping("/updateArticle")
    public ResponseEntity<ArticleResponse> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest){
        return articleService.updateArticle(articleUpdateRequest);
    }

    @DeleteMapping("/deleteArticle/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable String id){
        return articleService.deleteArticleById(UUID.fromString(id));
    }

}
