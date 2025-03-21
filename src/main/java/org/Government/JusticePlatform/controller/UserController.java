package org.Government.JusticePlatform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.Government.JusticePlatform.model.Role;
import org.Government.JusticePlatform.model.User;
import org.Government.JusticePlatform.security.jwt.JwtUtil;
import org.Government.JusticePlatform.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final JwtUtil jwtUtil;

    public UserController(IUserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {

            if (user.getRole() == null) {
                user.setRole(Role.CITIZEN);
            }
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error during user registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            System.out.println("Attempting to log in user with email: " + user.getEmail());

            boolean isAuthenticated = userService.authenticateUser(user.getEmail(), user.getPassword());

            System.out.println("Authentication successful: " + isAuthenticated);

            if (isAuthenticated) {
                User authenticatedUser = userService.getUser(user.getEmail());

                System.out.println("Authenticated user: " + authenticatedUser);

                String token = jwtUtil.generateToken(authenticatedUser);

                Map<String, String> response = new HashMap<String, String>();
                response.put("message", "Login successful!");
                response.put("token", token);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        try {

            String jwtToken = authorizationHeader.substring(7);
            Claims claims = jwtUtil.extractClaims(jwtToken);

            String userRole = claims.get("role", String.class);

            if (!Role.ADMIN.name().equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Insufficient privileges");
            }

            List<User> users = userService.getUsers();
            return ResponseEntity.ok(users);

        } catch (JwtException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the users");
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        try {
            User theUser = userService.getUser(email);
            return ResponseEntity.ok(theUser);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("email") String email) {
        try {

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is required");
            }

            String jwtToken = authorizationHeader.substring(7);
            Claims claims = jwtUtil.extractClaims(jwtToken);

            String userRole = claims.get("role", String.class);
            String tokenUserEmail = claims.getSubject();

            if (!Role.ADMIN.name().equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied: Only administrators can delete users");
            }

            if (email.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot delete your own admin account");
            }

            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }
}
