package org.Government.JusticePlatform.service;

import org.Government.JusticePlatform.model.Comment;
import org.Government.JusticePlatform.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Integer commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUser_Id(userId);
    }

    public List<Comment> getCommentsByArticleId(Integer articleId) {
        return commentRepository.findByArticle_ArticleId(articleId);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Integer commentId, Comment updatedComment) {
        return commentRepository.findById(commentId)
                .map(existingComment -> {
                    existingComment.setContent(updatedComment.getContent());
                    existingComment.setUpdatedAt(new java.util.Date()); 
                    return commentRepository.save(existingComment);
                }).orElse(null);
    }

    public boolean deleteComment(Integer commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}