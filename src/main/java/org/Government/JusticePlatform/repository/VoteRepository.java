package org.Government.JusticePlatform.repository;

import org.Government.JusticePlatform.model.Vote;
import org.Government.JusticePlatform.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    List<Vote> findByUserId(Integer userId);

    List<Vote> findByArticle_ArticleId(Integer articleId);

    long countByArticle_ArticleIdAndVoteType(Integer articleId, VoteType voteType);

    Optional<Vote> findByUser_IdAndArticle_ArticleId(Integer userId, Integer articleId);
}
