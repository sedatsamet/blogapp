package com.sedat.bootcamp.blogapp.blogapp.service;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleUpdateRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.ArticleResponse;
import com.sedat.bootcamp.blogapp.blogapp.entity.Article;
import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    public List<ArticleResponse> getAll() {
        List<Article> articleList = articleRepository.findAllByOrderByDateDesc();
        return orderAllArticleByDateDesc(articleList);
    }

    public ArticleResponse findById(UUID id) {
        return createArticleResponse(articleRepository.findById(id).orElse(null));
    }

    public ArticleResponse createArticle(ArticleRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(authentication.isAuthenticated()){
            Article newArticle = Article.builder()
                    .title(request.title())
                    .content(request.content())
                    .date(new Date())
                    .user(user)
                    .comments(new ArrayList<>())
                    .build();
            articleRepository.save(newArticle);
            return createArticleResponse(newArticle);
        }
        throw new UsernameNotFoundException("Invalid Username " + user.getUsername());
    }

    public ResponseEntity<ArticleResponse> updateArticle(ArticleUpdateRequest articleUpdateRequest){
        Optional<Article> optionalArticle = articleRepository.findById(articleUpdateRequest.articleId());
        if(optionalArticle.isPresent()){
            Article article = optionalArticle.get();
            User user = article.getUser();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication.isAuthenticated() && user.getUsername().equals(authentication.getName())){
                article.setTitle(articleUpdateRequest.title());
                article.setContent(articleUpdateRequest.content());
                article.setDate(new Date());
                articleRepository.save(article);
                return ResponseEntity.ok(createArticleResponse(article));
            }
        }
        return ResponseEntity.status(401).build();
    }

    public ResponseEntity<String> deleteArticleById(UUID articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if(optionalArticle.isPresent()){
            Article article = optionalArticle.get();
            User user = article.getUser();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication.isAuthenticated() && user.getUsername().equals(authentication.getName())){
                articleRepository.delete(article);
                return ResponseEntity.ok("Article that id: " + articleId + " deleted");
            }
        }
        return ResponseEntity.status(401).build();
    }

    private ArticleResponse createArticleResponse(Article article){
        ArticleResponse articleResponse = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getUser().getName())
                .date(article.getDate())
                .commentList(article.getComments())
                .build();
        return articleResponse;
    }

    private List<ArticleResponse> orderAllArticleByDateDesc(List<Article> articleList){
        return articleList.stream().map(article -> ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getUser().getName())
                .date(article.getDate())
                .commentList(article.getComments())
                .build()).toList();
    }
}
