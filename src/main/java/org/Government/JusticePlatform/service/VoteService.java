package org.Government.JusticePlatform.service;

import org.Government.JusticePlatform.model.Vote;
import org.Government.JusticePlatform.model.VoteType;
import org.Government.JusticePlatform.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public List<Vote> getVotesByUserId(Integer userId) {
        return voteRepository.findByUserId(userId);
    }

    public List<Vote> getVotesByArticleId(Integer articleId) {
        return voteRepository.findByArticle_ArticleId(articleId);
    }

    public long getVoteCountByArticleIdAndType(Integer articleId, VoteType voteType) {
        return voteRepository.countByArticle_ArticleIdAndVoteType(articleId, voteType);
    }

    public Vote createOrUpdateVote(Vote vote) {
        Optional<Vote> existingVote = voteRepository.findByUser_IdAndArticle_ArticleId(
                vote.getUser().getId().intValue(),
                vote.getArticle().getArticleId());

        if (existingVote.isPresent()) {
            Vote updatedVote = existingVote.get();
            updatedVote.setVoteType(vote.getVoteType());
            updatedVote.setCreatedAt(new java.util.Date());
            return voteRepository.save(updatedVote);
        } else {
            vote.setCreatedAt(new java.util.Date());
            return voteRepository.save(vote);
        }
    }

    public boolean removeVote(Integer voteId) {
        if (voteRepository.existsById(voteId)) {
            voteRepository.deleteById(voteId);
            return true;
        }
        return false;
    }
}
