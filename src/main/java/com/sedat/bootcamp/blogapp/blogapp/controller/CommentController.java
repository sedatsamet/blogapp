package com.sedat.bootcamp.blogapp.blogapp.controller;

import com.sedat.bootcamp.blogapp.blogapp.dto.request.CreateCommentRequest;
import com.sedat.bootcamp.blogapp.blogapp.dto.response.CommentResponse;
import com.sedat.bootcamp.blogapp.blogapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @GetMapping("/getAllComments")
    public ResponseEntity<List<CommentResponse>> getAll(){
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable String id){
        return ResponseEntity.ok(commentService.findById(UUID.fromString(id)));
    }

    @PostMapping("/createComment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest request){
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable String commentId){
        return commentService.deleteCommentById(UUID.fromString(commentId));
    }
}
