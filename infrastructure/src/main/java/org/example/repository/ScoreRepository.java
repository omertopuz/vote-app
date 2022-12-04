package org.example.repository;

import org.example.model.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<ScoreEntity,Integer> {
    List<ScoreEntity> getScoreByElectionId(Integer electionId);
    Optional<ScoreEntity> getScoreByElectionIdAndCandidateId(Integer electionId,Integer candidateId);
}
