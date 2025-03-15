package org.Government.JusticePlatform.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.Government.JusticePlatform.model.Comment;
import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.service.CommentService;
import org.Government.JusticePlatform.controller.CommentController;
import org.Government.JusticePlatform.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommentControllerTests {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment testComment1;
    private Comment testComment2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testComment1 = new Comment();
        testComment1.setCommentId(1);
        testComment1.setContent("This is test comment 1");
        Article testArticle1 = new Article();
        testArticle1.setTitle("This is test article 1");
        testComment1.setArticle(testArticle1);
        User testUser1 = new User();
        testUser1.setName("This is test user 1");
        testComment1.setUser(testUser1);

        testComment2 = new Comment();
        testComment2.setCommentId(2);
        testComment2.setContent("This is test comment 2");
        Article testArticle2 = new Article();
        testArticle2.setTitle("This is test article 2");
        testComment2.setArticle(testArticle2);
        User testUser2 = new User();
        testUser2.setName("This is test user 2");
        testComment2.setUser(testUser2);
    }

    @Test
    public void testGetAllComments() {
        
        List<Comment> expectedComments = Arrays.asList(testComment1, testComment2);
        when(commentService.getAllComments()).thenReturn(expectedComments);

     
        List<Comment> actualComments = commentController.getAllComments();

     
        assertEquals(expectedComments, actualComments);
        assertEquals(2, actualComments.size());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void testGetAllComments_EmptyList() {
      
        when(commentService.getAllComments()).thenReturn(Collections.emptyList());

     
        List<Comment> actualComments = commentController.getAllComments();

      
        assertTrue(actualComments.isEmpty());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void testGetCommentsByArticleId() {
    
        Integer articleId = 101;
        List<Comment> expectedComments = Arrays.asList(testComment1);
        when(commentService.getCommentsByArticleId(articleId)).thenReturn(expectedComments);

     
        List<Comment> actualComments = commentController.getCommentsByArticleId(articleId);

     
        assertEquals(expectedComments, actualComments);
        assertEquals(1, actualComments.size());
        verify(commentService, times(1)).getCommentsByArticleId(articleId);
    }

    @Test
    public void testGetCommentsByUserId() {
      
        Integer userId = 201;
        List<Comment> expectedComments = Arrays.asList(testComment1);
        when(commentService.getCommentsByUserId(userId)).thenReturn(expectedComments);

     
        List<Comment> actualComments = commentController.getCommentsByUserId(userId);

        assertEquals(expectedComments, actualComments);
        assertEquals(1, actualComments.size());

        verify(commentService, times(1)).getCommentsByUserId(userId);
    }

    @Test
    public void testGetCommentById_Found() {
    
        Integer commentId = 1;
        when(commentService.getCommentById(commentId)).thenReturn(Optional.of(testComment1));

  
        ResponseEntity<Comment> response = commentController.getCommentById(commentId);

   
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testComment1, response.getBody());
        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    public void testGetCommentById_NotFound() {
        
        Integer commentId = 999;
        when(commentService.getCommentById(commentId)).thenReturn(Optional.empty());

     
        ResponseEntity<Comment> response = commentController.getCommentById(commentId);

   
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    public void testCreateComment() {
     
        Comment newComment = new Comment();
        newComment.setContent("New Comment");
        Article newArticle = new Article();
        newArticle.setTitle("New Article");
        newComment.setArticle(newArticle);
        User newUser = new User();
        newUser.setName("New User");
        newComment.setUser(newUser);

        Comment savedComment = new Comment();
        savedComment.setCommentId(3);
        savedComment.setContent("New Comment");

        Article savedArticle = new Article();
        savedArticle.setTitle("New Article");
        savedComment.setArticle(savedArticle);

        User savedUser = new User();
        savedUser.setName("New User");
        savedComment.setUser(savedUser);

        when(commentService.createComment(newComment)).thenReturn(savedComment);

      
        Comment result = commentController.createComment(newComment);


        assertNotNull(result);
        assertEquals(3, result.getCommentId());
        assertEquals(newComment.getContent(), result.getContent());
        assertEquals(newComment.getArticle().getTitle(), result.getArticle().getTitle());
        assertEquals(newComment.getUser().getName(), result.getUser().getName());
        verify(commentService, times(1)).createComment(newComment);
    }

    @Test
    public void testUpdateComment_Success() {
  
        Integer commentId = 1;
        Comment updatedComment = new Comment();
        updatedComment.setContent("Updated Content");
        Article updatedArticle = new Article();
        updatedArticle.setTitle("updated article");
        updatedComment.setArticle(updatedArticle);
        User updatedUser = new User();
        updatedUser.setName("updated User");
        updatedComment.setUser(updatedUser);

        Comment returnedComment = new Comment();
        returnedComment.setCommentId(commentId);
        returnedComment.setContent("Updated Content");
        returnedComment.setArticle(updatedArticle);
        returnedComment.setUser(updatedUser);

        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(returnedComment);

      
        ResponseEntity<Comment> response = commentController.updateComment(commentId, updatedComment);

       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(commentId, response.getBody().getCommentId());
        assertEquals("Updated Content", response.getBody().getContent());
        verify(commentService, times(1)).updateComment(eq(commentId), any(Comment.class));
    }

    @Test
    public void testUpdateComment_NotFound() {
        
        Integer commentId = 999;
        Comment updatedComment = new Comment();
        updatedComment.setContent("Updated Content");

        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(null);

        
        ResponseEntity<Comment> response = commentController.updateComment(commentId, updatedComment);

      
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService, times(1)).updateComment(eq(commentId), any(Comment.class));
    }

    @Test
    public void testDeleteComment_Success() {
    
        Integer commentId = 1;
        when(commentService.deleteComment(commentId)).thenReturn(true);

       
        ResponseEntity<Void> response = commentController.deleteComment(commentId);

       
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(commentId);
    }

    @Test
    public void testDeleteComment_NotFound() {
       
        Integer commentId = 999;
        when(commentService.deleteComment(commentId)).thenReturn(false);

    
        ResponseEntity<Void> response = commentController.deleteComment(commentId);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(commentId);
    }
}
