package org.example.adapters.outbound.persistence;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.Score;
import org.example.domain.model.ScoreCandidate;
import org.example.domain.model.ScoreRequest;
import org.example.domain.model.UserVote;
import org.example.model.entity.ScoreEntity;
import org.example.model.entity.UserVoteEntity;
import org.example.ports.outbounds.VotePersistence;
import org.example.repository.ScoreRepository;
import org.example.repository.UserVoteRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VotePersistenceImpl implements VotePersistence {
    private final UserVoteRepository userVoteRepository;
    private final ScoreRepository scoreRepository;

    @Override
    public void saveUserVote(UserVote userVote) {
        UserVoteEntity voteEntity = new UserVoteEntity();
        voteEntity.setElectionId(userVote.getElectionId());
        voteEntity.setUserId(userVote.getUserId());
        voteEntity.setCreatedAt(new Date());
        userVoteRepository.save(voteEntity);
    }

    @Override
    public Optional<UserVote> getUserVote(UserVote userVote) {
        return userVoteRepository
                .getUserVoteEntityByElectionIdAndUserId(userVote.getElectionId(), userVote.getUserId())
                .map(u -> new UserVote(u.getUserId(),u.getElectionId(), u.getCreatedAt()));
    }

    @Override
    public Score getScore(ScoreRequest scoreRequest) {
        List<ScoreCandidate> scoreCandidates = scoreRepository.getScoreByElectionId(scoreRequest.getElectionId())
                .stream().map(s -> new ScoreCandidate(s.getCandidateId(), s.getVoteCount()))
                .collect(Collectors.toList());
        return new Score(scoreRequest.getElectionId(), scoreCandidates);
    }

    @Override
    public void updateScore(Integer electionId, Integer candidateId) {
        scoreRepository.getScoreByElectionIdAndCandidateId(electionId,candidateId)
                .map(scoreEntity -> {
                    scoreEntity.setVoteCount(scoreEntity.getVoteCount() + 1);
                    scoreRepository.save(scoreEntity);
                    return scoreEntity;
                })
                .orElseGet(()->{
                    ScoreEntity scoreEntity = new ScoreEntity();
                    scoreEntity.setElectionId(electionId);
                    scoreEntity.setCandidateId(candidateId);
                    scoreEntity.setVoteCount(1);
                    scoreRepository.save(scoreEntity);
                    return scoreEntity;
                });
    }
}
