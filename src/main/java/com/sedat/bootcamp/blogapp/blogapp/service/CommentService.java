package com.sedat.bootcamp.blogapp.blogapp.service;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.CreateCommentRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.CommentResponse;
import com.sedat.bootcamp.blogapp.blogapp.entity.Article;
import com.sedat.bootcamp.blogapp.blogapp.entity.Comment;
import com.sedat.bootcamp.blogapp.blogapp.entity.User;
import com.sedat.bootcamp.blogapp.blogapp.repository.ArticleRepository;
import com.sedat.bootcamp.blogapp.blogapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<CommentResponse> getAllComments(){
        List<Comment> commentList = commentRepository.findAll();
        return orderAllCommentsByDateDesc(commentList);
    }

    public CommentResponse findById(UUID id){
        return createCommentResponse(commentRepository.findById(id).orElse(null));
    }

    public CommentResponse createComment(CreateCommentRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<Article> articleFromDb = articleRepository.findById(UUID.fromString(request.articleId()));

        if(authentication.isAuthenticated() && articleFromDb.isPresent()){
            Article article = articleFromDb.get();
            Comment comment = Comment.builder()
                    .content(request.content())
                    .article(article)
                    .date(new Date())
                    .author(user.getName())
                    .user(user)
                    .build();
            List<Comment> commentList = article.getComments();
            commentList.add(comment);
            article.setComments(commentList);
            commentRepository.saveAndFlush(comment);
            return createCommentResponse(comment);
        }
        return null;
    }

    public ResponseEntity<String> deleteCommentById(UUID commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            User user = comment.getUser();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication.isAuthenticated() && user.getUsername().equals(authentication.getName())){
                commentRepository.delete(comment);
                return ResponseEntity.ok("Comment that id: " + commentId + " deleted");
            }
        }
        return ResponseEntity.status(401).build();
    }

    private List<CommentResponse> orderAllCommentsByDateDesc(List<Comment> commentList){
        return commentList.stream().map(comment -> CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(comment.getUser().getUsername())
                .date(comment.getDate())
                .build()).toList();
    }

    private CommentResponse createCommentResponse(Comment comment){
        CommentResponse commentResponse = CommentResponse.builder()
                .commentId(comment.getId())
                .author(comment.getUser().getUsername())
                .content(comment.getContent())
                .date(comment.getDate())
                .build();
        return commentResponse;
    }
}
