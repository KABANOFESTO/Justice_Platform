package org.Government.JusticePlatform.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.Government.JusticePlatform.model.Vote;
import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.model.User;
import org.Government.JusticePlatform.model.VoteType;
import org.Government.JusticePlatform.service.VoteService;
import org.Government.JusticePlatform.controller.VoteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VoteControllerTest {

    @Mock
    private VoteService voteService;

    @InjectMocks
    private VoteController voteController;

    private Vote upvote;
    private Vote downvote;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        upvote = new Vote();
        upvote.setVoteId(1);
        User user = new User();
        user.setName("kabano");
        upvote.setUser(user);
        Article article = new Article();
        article.setTitle("article");
        upvote.setArticle(article);
        upvote.setVoteType(VoteType.UP);

        downvote = new Vote();
        downvote.setVoteId(2);
        User user2 = new User();
        user2.setName("kabano2");
        downvote.setUser(user2);
        Article article2 = new Article();
        article2.setTitle("article2");
        downvote.setArticle(article2);
        downvote.setVoteType(VoteType.DOWN);
    }

    @Test
    public void testGetAllVotes() {
        // Arrange
        List<Vote> expectedVotes = Arrays.asList(upvote, downvote);
        when(voteService.getAllVotes()).thenReturn(expectedVotes);

        // Act
        List<Vote> actualVotes = voteController.getAllVotes();

        // Assert
        assertEquals(expectedVotes, actualVotes);
        assertEquals(2, actualVotes.size());
        verify(voteService, times(1)).getAllVotes();
    }

    @Test
    public void testGetAllVotes_EmptyList() {
        // Arrange
        when(voteService.getAllVotes()).thenReturn(Collections.emptyList());

        // Act
        List<Vote> actualVotes = voteController.getAllVotes();

        // Assert
        assertTrue(actualVotes.isEmpty());
        verify(voteService, times(1)).getAllVotes();
    }

    @Test
    public void testGetVotesByUserId() {
        // Arrange
        Integer userId = 101;
        List<Vote> expectedVotes = Collections.singletonList(upvote);
        when(voteService.getVotesByUserId(userId)).thenReturn(expectedVotes);

        // Act
        List<Vote> actualVotes = voteController.getVotesByUserId(userId);

        // Assert
        assertEquals(expectedVotes, actualVotes);
        assertEquals(1, actualVotes.size());
        assertEquals(upvote, actualVotes.get(0));
        verify(voteService, times(1)).getVotesByUserId(userId);
    }

    @Test
    public void testGetVotesByUserId_NoVotes() {
        // Arrange
        Integer userId = 999;
        when(voteService.getVotesByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        List<Vote> actualVotes = voteController.getVotesByUserId(userId);

        // Assert
        assertTrue(actualVotes.isEmpty());
        verify(voteService, times(1)).getVotesByUserId(userId);
    }

    @Test
    public void testGetVotesByArticleId() {
        // Arrange
        Integer articleId = 201;
        List<Vote> expectedVotes = Arrays.asList(upvote, downvote);
        when(voteService.getVotesByArticleId(articleId)).thenReturn(expectedVotes);

        // Act
        List<Vote> actualVotes = voteController.getVotesByArticleId(articleId);

        // Assert
        assertEquals(expectedVotes, actualVotes);
        assertEquals(2, actualVotes.size());
        verify(voteService, times(1)).getVotesByArticleId(articleId);
    }

    @Test
    public void testGetVotesByArticleId_NoVotes() {
        // Arrange
        Integer articleId = 999;
        when(voteService.getVotesByArticleId(articleId)).thenReturn(Collections.emptyList());

        // Act
        List<Vote> actualVotes = voteController.getVotesByArticleId(articleId);

        // Assert
        assertTrue(actualVotes.isEmpty());
        verify(voteService, times(1)).getVotesByArticleId(articleId);
    }

    @Test
    public void testCountVotesByArticleAndType_Upvotes() {
        // Arrange
        Integer articleId = 201;
        VoteType voteType = VoteType.UP;
        long expectedCount = 5;
        when(voteService.getVoteCountByArticleIdAndType(articleId, voteType)).thenReturn(expectedCount);

        // Act
        long actualCount = voteController.countVotesByArticleAndType(articleId, voteType);

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(voteService, times(1)).getVoteCountByArticleIdAndType(articleId, voteType);
    }

    @Test
    public void testCountVotesByArticleAndType_Downvotes() {
        // Arrange
        Integer articleId = 201;
        VoteType voteType = VoteType.DOWN;
        long expectedCount = 3;
        when(voteService.getVoteCountByArticleIdAndType(articleId, voteType)).thenReturn(expectedCount);

        // Act
        long actualCount = voteController.countVotesByArticleAndType(articleId, voteType);

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(voteService, times(1)).getVoteCountByArticleIdAndType(articleId, voteType);
    }

    @Test
    public void testCountVotesByArticleAndType_NoVotes() {
        // Arrange
        Integer articleId = 999;
        VoteType voteType = VoteType.UP;
        long expectedCount = 0;
        when(voteService.getVoteCountByArticleIdAndType(articleId, voteType)).thenReturn(expectedCount);

        // Act
        long actualCount = voteController.countVotesByArticleAndType(articleId, voteType);

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(voteService, times(1)).getVoteCountByArticleIdAndType(articleId, voteType);
    }

    @Test
    public void testCreateOrUpdateVote() {
        // Arrange
        Vote newVote = new Vote();
        User newUser = new User();
        newUser.setName("kabano");
        newVote.setUser(newUser);
        Article newArticle = new Article();
        newArticle.setTitle("article");
        newVote.setArticle(newArticle);
        newVote.setVoteType(VoteType.UP);

        Vote savedVote = new Vote();
        savedVote.setVoteId(3);
        savedVote.setUser(newUser); 
        savedVote.setArticle(newArticle); 
        savedVote.setVoteType(VoteType.UP);

        when(voteService.createOrUpdateVote(newVote)).thenReturn(savedVote);

     
        Vote result = voteController.createOrUpdateVote(newVote);

 
        assertNotNull(result);
        assertEquals(3, result.getVoteId());
        assertEquals(newVote.getUser().getName(), result.getUser().getName()); 
        assertEquals(newVote.getArticle().getTitle(), result.getArticle().getTitle()); 
        assertEquals(newVote.getVoteType(), result.getVoteType());
        verify(voteService, times(1)).createOrUpdateVote(newVote);
    }

    @Test
    public void testCreateOrUpdateVote_ExistingVote() {
      
        Vote existingVote = new Vote();
        existingVote.setVoteId(1);
        User existingUser = new User();
        existingUser.setName("kabanoFK");
        existingVote.setUser(existingUser);
        Article existingArticle = new Article();
        existingArticle.setTitle("articleFK");
        existingVote.setArticle(existingArticle);
        existingVote.setVoteType(VoteType.DOWN); 

        Vote updatedVote = new Vote();
        updatedVote.setVoteId(1);
        User updatedUser = new User();
        updatedUser.setName("kabanoFK2");
        updatedVote.setUser(updatedUser);
        Article updatedArticle = new Article();
        updatedArticle.setTitle("articleFK2");
        updatedVote.setArticle(updatedArticle);
        updatedVote.setVoteType(VoteType.DOWN);

        when(voteService.createOrUpdateVote(existingVote)).thenReturn(updatedVote);

       
        Vote result = voteController.createOrUpdateVote(existingVote);

        assertNotNull(result);
        assertEquals(1, result.getVoteId());
        assertEquals(VoteType.DOWN, result.getVoteType());
        verify(voteService, times(1)).createOrUpdateVote(existingVote);
    }

    @Test
    public void testRemoveVote_Success() {
     
        Integer voteId = 1;
        when(voteService.removeVote(voteId)).thenReturn(true);

     
        ResponseEntity<Void> response = voteController.removeVote(voteId);

   
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(voteService, times(1)).removeVote(voteId);
    }

    @Test
    public void testRemoveVote_NotFound() {
      
        Integer voteId = 999;
        when(voteService.removeVote(voteId)).thenReturn(false);

       
        ResponseEntity<Void> response = voteController.removeVote(voteId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(voteService, times(1)).removeVote(voteId);
    }
}