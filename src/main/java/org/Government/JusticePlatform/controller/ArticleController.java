package org.Government.JusticePlatform.controller;

import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer articleId) {
        Optional<Article> article = articleService.getArticleById(articleId);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Article> getArticlesByUserId(@PathVariable Integer userId) {
        return articleService.getArticlesByAuthorId(userId);
    }

    @GetMapping("/category/{category}")
    public List<Article> getArticlesByCategory(@PathVariable String category) {
        return articleService.getArticlesByCategory(category);
    }

    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer articleId, @RequestBody Article updatedArticle) {
        Optional<Article> article = articleService.updateArticle(articleId, updatedArticle);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer articleId) {
        boolean deleted = articleService.deleteArticle(articleId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
