package org.Government.JusticePlatform.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.service.ArticleService;
import org.Government.JusticePlatform.controller.ArticleController;
import org.Government.JusticePlatform.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    private Article testArticle1;
    private Article testArticle2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testArticle1 = new Article();
        testArticle1.setArticleId(1);
        testArticle1.setTitle("Test Article 1");
        testArticle1.setContent("This is test content 1");
        User author1 = new User();
        author1.setName("kabano");
        testArticle1.setAuthor(author1);
        testArticle1.setCategory("legal");

        testArticle2 = new Article();
        testArticle2.setArticleId(2);
        testArticle2.setTitle("Test Article 2");
        testArticle2.setContent("This is test content 2");
        User author2 = new User();
        author2.setName("kabanoFesto");
        testArticle2.setAuthor(author2);
        testArticle2.setCategory("criminal");
    }

    @Test
    public void testGetAllArticles() {
        // Arrange
        List<Article> expectedArticles = Arrays.asList(testArticle1, testArticle2);
        when(articleService.getAllArticles()).thenReturn(expectedArticles);

        // Act
        List<Article> actualArticles = articleController.getAllArticles();

        // Assert
        assertEquals(expectedArticles, actualArticles);
        assertEquals(2, actualArticles.size());
        verify(articleService, times(1)).getAllArticles();
    }

    @Test
    public void testGetAllArticles_EmptyList() {
       
        when(articleService.getAllArticles()).thenReturn(Collections.emptyList());

       
        List<Article> actualArticles = articleController.getAllArticles();

       
        assertTrue(actualArticles.isEmpty());
        verify(articleService, times(1)).getAllArticles();
    }

    @Test
    public void testGetArticleById_Found() {
       
        Integer articleId = 1;
        when(articleService.getArticleById(articleId)).thenReturn(Optional.of(testArticle1));

       
        ResponseEntity<Article> response = articleController.getArticleById(articleId);

     
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testArticle1, response.getBody());
        verify(articleService, times(1)).getArticleById(articleId);
    }

    @Test
    public void testGetArticleById_NotFound() {
      
        Integer articleId = 999;
        when(articleService.getArticleById(articleId)).thenReturn(Optional.empty());

       
        ResponseEntity<Article> response = articleController.getArticleById(articleId);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(articleService, times(1)).getArticleById(articleId);
    }

    @Test
    public void testGetArticlesByUserId() {
      
        Integer userId = 101;
        List<Article> expectedArticles = Collections.singletonList(testArticle1);
        when(articleService.getArticlesByAuthorId(userId)).thenReturn(expectedArticles);

       
        List<Article> actualArticles = articleController.getArticlesByUserId(userId);

        assertEquals(expectedArticles, actualArticles);
        assertEquals(1, actualArticles.size());
        assertEquals(testArticle1, actualArticles.get(0));
        verify(articleService, times(1)).getArticlesByAuthorId(userId);
    }

    @Test
    public void testGetArticlesByUserId_NoArticles() {
        
        Integer userId = 999;
        when(articleService.getArticlesByAuthorId(userId)).thenReturn(Collections.emptyList());

       
        List<Article> actualArticles = articleController.getArticlesByUserId(userId);

      
        assertTrue(actualArticles.isEmpty());
        verify(articleService, times(1)).getArticlesByAuthorId(userId);
    }

    @Test
    public void testGetArticlesByCategory() {
        
        String category = "legal";
        List<Article> expectedArticles = Collections.singletonList(testArticle1);
        when(articleService.getArticlesByCategory(category)).thenReturn(expectedArticles);

     
        List<Article> actualArticles = articleController.getArticlesByCategory(category);

       
        assertEquals(expectedArticles, actualArticles);
        assertEquals(1, actualArticles.size());
        assertEquals(testArticle1, actualArticles.get(0));
        verify(articleService, times(1)).getArticlesByCategory(category);
    }

    @Test
    public void testGetArticlesByCategory_NoArticles() {
        
        String category = "nonexistent";
        when(articleService.getArticlesByCategory(category)).thenReturn(Collections.emptyList());

      
        List<Article> actualArticles = articleController.getArticlesByCategory(category);

       
        assertTrue(actualArticles.isEmpty());
        verify(articleService, times(1)).getArticlesByCategory(category);
    }

    @Test
    public void testCreateArticle() {
        
        Article newArticle = new Article();
        newArticle.setTitle("New Article");
        newArticle.setContent("New Content");
        User newAuthor = new User();
        newAuthor.setName("keza");
        newArticle.setAuthor(newAuthor);
        newArticle.setCategory("civil");

        Article savedArticle = new Article();
        savedArticle.setArticleId(3);
        savedArticle.setTitle("New Article");
        savedArticle.setContent("New Content");
        savedArticle.setAuthor(newAuthor); 
        savedArticle.setCategory("civil");

        when(articleService.createArticle(newArticle)).thenReturn(savedArticle);

       
        Article result = articleController.createArticle(newArticle);

      
        assertNotNull(result);
        assertEquals(3, result.getArticleId());
        assertEquals(newArticle.getTitle(), result.getTitle());
        assertEquals(newArticle.getContent(), result.getContent());
        assertEquals(newArticle.getAuthor().getName(), result.getAuthor().getName()); 
        assertEquals(newArticle.getCategory(), result.getCategory());
        verify(articleService, times(1)).createArticle(newArticle);
    }

    @Test
    public void testUpdateArticle_Found() {
       
        Integer articleId = 1;
        Article updatedArticle = new Article();
        updatedArticle.setArticleId(articleId);
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setContent("Updated Content");
        User updatedAuthor = new User();
        updatedAuthor.setName("KabanoUpadated");
        updatedArticle.setAuthor(updatedAuthor);
        updatedArticle.setCategory("updated");

        when(articleService.updateArticle(eq(articleId), any(Article.class))).thenReturn(Optional.of(updatedArticle));

    
        ResponseEntity<Article> response = articleController.updateArticle(articleId, updatedArticle);

    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedArticle, response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Content", response.getBody().getContent());
        verify(articleService, times(1)).updateArticle(eq(articleId), any(Article.class));
    }

    @Test
    public void testUpdateArticle_NotFound() {
       
        Integer articleId = 999;
        Article updatedArticle = new Article();
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setContent("Updated Content");

        when(articleService.updateArticle(eq(articleId), any(Article.class))).thenReturn(Optional.empty());

        
        ResponseEntity<Article> response = articleController.updateArticle(articleId, updatedArticle);

    
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(articleService, times(1)).updateArticle(eq(articleId), any(Article.class));
    }

    @Test
    public void testDeleteArticle_Success() {
      
        Integer articleId = 1;
        when(articleService.deleteArticle(articleId)).thenReturn(true);

        
        ResponseEntity<Void> response = articleController.deleteArticle(articleId);

     
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(articleService, times(1)).deleteArticle(articleId);
    }

    @Test
    public void testDeleteArticle_NotFound() {
    
        Integer articleId = 999;
        when(articleService.deleteArticle(articleId)).thenReturn(false);

       
        ResponseEntity<Void> response = articleController.deleteArticle(articleId);

       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(articleService, times(1)).deleteArticle(articleId);
    }
}