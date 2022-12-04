package org.example.repository;

import org.example.model.entity.UserVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVoteRepository extends JpaRepository<UserVoteEntity,Integer> {
    Optional<UserVoteEntity> getUserVoteEntityByElectionIdAndUserId(Integer electionId, String userId);
}
