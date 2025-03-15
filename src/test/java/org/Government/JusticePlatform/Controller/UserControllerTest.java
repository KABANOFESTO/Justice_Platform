package org.Government.JusticePlatform.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.Government.JusticePlatform.model.Role;
import org.Government.JusticePlatform.model.User;
import org.Government.JusticePlatform.security.jwt.JwtUtil;
import org.Government.JusticePlatform.controller.UserController;
import org.Government.JusticePlatform.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.Claims;

public class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Claims claims;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private User adminUser;
    private String validToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole(Role.CITIZEN);

        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("admin123");
        adminUser.setRole(Role.ADMIN);

        validToken = "valid.jwt.token";
    }


    @Test
    public void testSignup_Error() throws Exception {
        
        doThrow(new RuntimeException("Email already exists")).when(userService).createUser(any(User.class));

        ResponseEntity<String> response = userController.signup(testUser);

    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Error during user registration"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        
        when(userService.authenticateUser(testUser.getEmail(), testUser.getPassword())).thenReturn(true);
        when(userService.getUser(testUser.getEmail())).thenReturn(testUser);
        when(jwtUtil.generateToken(testUser)).thenReturn(validToken);

       
        ResponseEntity<?> response = userController.login(testUser);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();

       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful!", responseBody.get("message"));
        assertEquals(validToken, responseBody.get("token"));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        
        when(userService.authenticateUser(testUser.getEmail(), testUser.getPassword())).thenReturn(false);

    
        ResponseEntity<?> response = userController.login(testUser);

       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    public void testLogin_UserNotFound() throws Exception {
    
        when(userService.authenticateUser(testUser.getEmail(), testUser.getPassword()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        
        ResponseEntity<?> response = userController.login(testUser);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void testGetAllUsers_Admin() {
    
        List<User> userList = Arrays.asList(testUser, adminUser);
        when(jwtUtil.extractClaims("valid.token")).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("ADMIN");
        when(userService.getUsers()).thenReturn(userList);

      
        ResponseEntity<?> response = userController.getAllUsers("Bearer valid.token");
        List<User> responseBody = (List<User>) response.getBody();

       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, responseBody.size());
        verify(userService).getUsers();
    }

    @Test
    public void testGetAllUsers_NonAdmin() {
     
        when(jwtUtil.extractClaims("valid.token")).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("CITIZEN");

       
        ResponseEntity<?> response = userController.getAllUsers("Bearer valid.token");

       
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied: Insufficient privileges", response.getBody());
        verify(userService, never()).getUsers();
    }

    @Test
    public void testGetAllUsers_InvalidToken() {
        
        when(jwtUtil.extractClaims("invalid.token")).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));

     
        ResponseEntity<?> response = userController.getAllUsers("Bearer invalid.token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
    }

    @Test
    public void testGetUserByEmail_Success() {
       
        when(userService.getUser(testUser.getEmail())).thenReturn(testUser);

    
        ResponseEntity<?> response = userController.getUserByEmail(testUser.getEmail());
        User responseBody = (User) response.getBody();

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, responseBody);
    }

    @Test
    public void testGetUserByEmail_NotFound() {
       
        String nonExistentEmail = "nonexistent@example.com";
        when(userService.getUser(nonExistentEmail)).thenThrow(new UsernameNotFoundException("User not found"));

    
        ResponseEntity<?> response = userController.getUserByEmail(nonExistentEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }

    @Test
    public void testDeleteUser_Admin() {
       
        when(jwtUtil.extractClaims("valid.token")).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("ADMIN");
        when(claims.getSubject()).thenReturn("admin@example.com");
        doNothing().when(userService).deleteUser(testUser.getEmail());

      
        ResponseEntity<String> response = userController.deleteUser("Bearer valid.token", testUser.getEmail());


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService).deleteUser(testUser.getEmail());
    }

    @Test
    public void testDeleteUser_AdminDeleteSelf() {
    
        when(jwtUtil.extractClaims("valid.token")).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("ADMIN");
        when(claims.getSubject()).thenReturn("admin@example.com");

     
        ResponseEntity<String> response = userController.deleteUser("Bearer valid.token", "admin@example.com");

      
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Cannot delete your own admin account", response.getBody());
        verify(userService, never()).deleteUser(anyString());
    }

    @Test
    public void testDeleteUser_NonAdmin() {
      
        when(jwtUtil.extractClaims("valid.token")).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("CITIZEN");

        ResponseEntity<String> response = userController.deleteUser("Bearer valid.token", testUser.getEmail());

    
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied: Only administrators can delete users", response.getBody());
        verify(userService, never()).deleteUser(anyString());
    }

    @Test
    public void testDeleteUser_InvalidToken() {
     
        when(jwtUtil.extractClaims("invalid.token")).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));

   
        ResponseEntity<String> response = userController.deleteUser("Bearer invalid.token", testUser.getEmail());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
        verify(userService, never()).deleteUser(anyString());
    }

    @Test
    public void testDeleteUser_NoToken() {
       
        ResponseEntity<String> response = userController.deleteUser(null, testUser.getEmail());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authorization token is required", response.getBody());
        verify(userService, never()).deleteUser(anyString());
    }
}
