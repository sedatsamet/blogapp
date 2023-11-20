package com.sedat.bootcamp.blogapp.blogapp.service;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.ArticleRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.ArticleResponse;
import com.sedat.bootcamp.blogapp.blogapp.entity.Article;
import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleResponse> getAll() {
        List<Article> articleList = articleRepository.findAllByOrderByDateDesc();
        return articleList.stream().map(article -> ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getUser().getName())
                .date(article.getDate())
                .commentList(article.getComments())
                .build()).toList();
    }

    public ArticleResponse createArticle(ArticleRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if(authentication.isAuthenticated()){
            Article newArticle = Article.builder()
                    .title(request.title())
                    .content(request.content())
                    .date(new Date())
                    .user(user)
                    .comments(List.of())
                    .build();
            articleRepository.save(newArticle);
            return createArticleResponse(newArticle);
        }
        throw new UsernameNotFoundException("Invalid Username " + user.getUsername());
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
}
