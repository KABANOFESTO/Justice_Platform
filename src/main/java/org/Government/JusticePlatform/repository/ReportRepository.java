package org.Government.JusticePlatform.repository;

import org.Government.JusticePlatform.model.Report;
import org.Government.JusticePlatform.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByUserId(Integer userId);

    List<Report> findByArticle_ArticleId(Integer articleId);

    List<Report> findByComment_CommentId(Integer commentId);

    List<Report> findByStatus(Status status);

    long countByStatus(Status status);
}
