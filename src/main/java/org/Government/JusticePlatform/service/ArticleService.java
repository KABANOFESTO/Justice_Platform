package org.Government.JusticePlatform.service;

import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Integer id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByAuthorId(Integer authorId) {
        return articleRepository.findByAuthorId(authorId);
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategory(category);
    }

    public List<Article> searchArticlesByTitle(String keyword) {
        return articleRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> updateArticle(Integer id, Article updatedArticle) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(updatedArticle.getTitle());
            article.setContent(updatedArticle.getContent());
            article.setCategory(updatedArticle.getCategory());
            article.setUpdatedAt(new java.util.Date());
            return articleRepository.save(article);
        });
    }

    public boolean deleteArticle(Integer id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
