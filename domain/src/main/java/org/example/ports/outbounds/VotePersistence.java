package org.example.ports.outbounds;

import org.example.domain.model.Score;
import org.example.domain.model.ScoreRequest;
import org.example.domain.model.UserVote;

import java.util.Optional;

public interface VotePersistence {

    void saveUserVote(UserVote userVote);
    Optional<UserVote> getUserVote(UserVote userVote);
    Score getScore(ScoreRequest scoreRequest);

    void updateScore(Integer electionId, Integer candidateId);

}
