package org.Government.JusticePlatform.repository;

import org.Government.JusticePlatform.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

   
    List<Article> findByAuthorId(Integer authorId);

    List<Article> findByCategory(String category);

    List<Article> findByTitleContainingIgnoreCase(String keyword);

    List<Article> findByCreatedAtAfter(java.util.Date date);

    List<Article> findTop10ByOrderByCreatedAtDesc();
}
