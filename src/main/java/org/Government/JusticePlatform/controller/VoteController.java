package org.Government.JusticePlatform.controller;

import org.Government.JusticePlatform.model.Vote;
import org.Government.JusticePlatform.model.VoteType;
import org.Government.JusticePlatform.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

    @GetMapping("/user/{userId}")
    public List<Vote> getVotesByUserId(@PathVariable Integer userId) {
        return voteService.getVotesByUserId(userId);
    }

    @GetMapping("/article/{articleId}")
    public List<Vote> getVotesByArticleId(@PathVariable Integer articleId) {
        return voteService.getVotesByArticleId(articleId);
    }

    @GetMapping("/article/{articleId}/count/{voteType}")
    public long countVotesByArticleAndType(@PathVariable Integer articleId, @PathVariable VoteType voteType) {
        return voteService.getVoteCountByArticleIdAndType(articleId, voteType);
    }

    @PostMapping
    public Vote createOrUpdateVote(@RequestBody Vote vote) {
        return voteService.createOrUpdateVote(vote);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> removeVote(@PathVariable Integer voteId) {
        boolean deleted = voteService.removeVote(voteId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
