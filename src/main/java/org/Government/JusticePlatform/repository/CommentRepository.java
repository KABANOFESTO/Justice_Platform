package org.Government.JusticePlatform.repository;

import org.Government.JusticePlatform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByUser_Id(Integer userId);

    List<Comment> findByArticle_ArticleId(Integer articleId);

    List<Comment> findByContentContainingIgnoreCase(String keyword);

    List<Comment> findByCreatedAtAfter(java.util.Date date);

    long countByArticle_ArticleId(Integer articleId);

    long countByUser_Id(Integer userId);
}